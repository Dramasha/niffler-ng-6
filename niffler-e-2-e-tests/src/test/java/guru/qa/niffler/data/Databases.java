package guru.qa.niffler.data;

import com.atomikos.icatch.jta.UserTransactionImp;
import com.atomikos.jdbc.AtomikosDataSourceBean;
import jakarta.transaction.SystemException;
import jakarta.transaction.UserTransaction;
import org.apache.commons.lang3.StringUtils;
import org.postgresql.ds.PGSimpleDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;
import java.util.function.Function;

public class Databases {
    private Databases() {
    }

    private static final Map<String, DataSource> dataSources = new ConcurrentHashMap<>();
    private static final Map<Long, Map<String, Connection>> threadConnections = new ConcurrentHashMap<>();

    public record XaFunction<T>(Function<Connection, T> function, String jdbcUrl) {}


    public record XaConsumer(Consumer<Connection> consumer, String jdbcUrl) {}


    public static <T> T transaction(Function<Connection, T> function, String jdbcUrl) {
        Connection connection = null;
        try {
            connection = connection(jdbcUrl);
            connection.setAutoCommit(false);
            T result = function.apply(connection);
            connection.commit();
            connection.setAutoCommit(true);
            return result;
        } catch (SQLException e) {
            if (connection != null) {
                try {
                    connection.rollback();
                    connection.setAutoCommit(true);
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            }
            throw new RuntimeException(e);
        }
    }

    public static <T> T xaTransaction(XaFunction<T>... actions) {
        UserTransaction ut = new UserTransactionImp();
        try {
            ut.begin();
            T result = null;
            for (XaFunction<T> action : actions) {
                result = action.function.apply(connection(action.jdbcUrl));
            }
            ut.commit();
            return result;
        } catch (Exception e) {
            try {
                ut.rollback();

            } catch (SystemException ex) {
                throw new RuntimeException(ex);
            }
            throw new RuntimeException(e);
        }
    }

    public static void transaction(Consumer<Connection> consumer, String jdbcUrl) {
        Connection connection = null;
        try {
            connection = connection(jdbcUrl);
            connection.setAutoCommit(false);
            consumer.accept(connection);
            connection.commit();
            connection.setAutoCommit(true);
        } catch (SQLException e) {
            if (connection != null) {
                try {
                    connection.rollback();
                    connection.setAutoCommit(true);
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            }
            throw new RuntimeException(e);
        }
    }

    public static void xaConsumer(XaConsumer... actions) {
        UserTransaction ut = new UserTransactionImp();
        try {
            ut.begin();
            for (XaConsumer action : actions) {
                action.consumer.accept(connection(action.jdbcUrl));
            }
            ut.commit();
        } catch (Exception e) {
            try {
                ut.rollback();

            } catch (SystemException ex) {
                throw new RuntimeException(ex);
            }
            throw new RuntimeException(e);
        }
    }


        private static DataSource getDataSource (String jdbcUrl){
            return dataSources.computeIfAbsent(
                    jdbcUrl,
                    key -> {
                        AtomikosDataSourceBean dsBean = new AtomikosDataSourceBean();
                        final String uniqId = StringUtils.substringAfter(jdbcUrl, "5432/");
                        dsBean.setUniqueResourceName(uniqId);
                        dsBean.setXaDataSourceClassName("org.postgresql.xa.PGXADataSource");
                        Properties properties = new Properties();
                        properties.put("URL", jdbcUrl);
                        properties.put("user", "postgres");
                        properties.put("password", "secret");
                        dsBean.setXaProperties(properties);
                        dsBean.setPoolSize(10);
                        return dsBean;
                    }
            );
        }

        public static Connection connection (String jdbcUrl) throws SQLException {
            return threadConnections.computeIfAbsent(
                    Thread.currentThread().threadId(),
                    key -> {
                        try {
                            return new HashMap<>(Map.of(
                                    jdbcUrl,
                                    getDataSource(jdbcUrl).getConnection()
                            ));
                        } catch (SQLException e) {
                            throw new RuntimeException(e);
                        }
                    }
            ).computeIfAbsent(
                    jdbcUrl,
                    key -> {
                        try {
                            return getDataSource(jdbcUrl).getConnection();
                        } catch (SQLException e) {
                            throw new RuntimeException(e);
                        }
                    }
            );
        }

        public static void closeAllConnections () {
            for (Map<String, Connection> connectionsMap : threadConnections.values()) {
                for (Connection connection : connectionsMap.values()) {
                    try {
                        if (connection != null && !connection.isClosed()) {
                            connection.close();
                        }
                    } catch (SQLException e) {
                        //NOP
                    }
                }
            }
        }
    }
