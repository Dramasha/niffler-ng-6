package guru.qa.niffler.data.tpl;

import java.util.ArrayList;
import java.util.List;

public class JdbcConnectionHolders implements AutoCloseable {

    private final List<JdbcConnectionHolder> holders = new ArrayList<>();

    public JdbcConnectionHolders(List<JdbcConnectionHolder> holders) {
        this.holders.addAll(holders);
    }

    @Override
    public void close() {
        holders.forEach(JdbcConnectionHolder::close);
    }
}
