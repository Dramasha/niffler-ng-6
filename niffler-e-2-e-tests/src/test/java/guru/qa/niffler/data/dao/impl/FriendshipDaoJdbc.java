package guru.qa.niffler.data.dao.impl;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.dao.FriendshipDao;
import guru.qa.niffler.data.entity.userdata.FriendshipEntity;
import guru.qa.niffler.data.entity.userdata.FriendshipStatus;
import guru.qa.niffler.data.entity.userdata.UserEntity;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static guru.qa.niffler.data.tpl.Connections.holder;

public class FriendshipDaoJdbc implements FriendshipDao {

    private static final Config CFG = Config.getInstance();

    @Override
    public void create(FriendshipEntity friendship) {
        try (PreparedStatement statement = holder(CFG.userdataJdbcUrl()).connection().prepareStatement(
                "INSERT INTO friendship (requester_id, addressee_id, status, created_date) " +
                        "VALUES (?,?,?,?)"
        )) {
            statement.setObject(1, friendship.getRequester().getId());
            statement.setObject(2, friendship.getAddressee().getId());
            statement.setString(3, friendship.getStatus().name());
            statement.setDate(4, new java.sql.Date(friendship.getCreatedDate().getTime()));

            statement.executeUpdate();


        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<FriendshipEntity> findByRequesterId(UUID requesterId) {
        try (PreparedStatement statement = holder(CFG.userdataJdbcUrl()).connection().prepareStatement(
                "SELECT * FROM friendship WHERE requester_id = ?"
        )) {
            statement.setObject(1, requesterId);
            statement.execute();
            try (ResultSet resultSet = statement.getResultSet()) {
                if (resultSet.next()) {
                    FriendshipEntity friendship = new FriendshipEntity();

                    friendship.setRequester(resultSet.getObject("requester_id", UserEntity.class));
                    friendship.setAddressee(resultSet.getObject("addressee_id", UserEntity.class));
                    friendship.setStatus(resultSet.getObject("status", FriendshipStatus.class));
                    friendship.setCreatedDate(resultSet.getDate("created_date"));


                    return Optional.of(friendship);
                } else {
                    return Optional.empty();
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<FriendshipEntity> findByAddresseeId(UUID addresseeId) {
        try (PreparedStatement statement = holder(CFG.userdataJdbcUrl()).connection().prepareStatement(
                "SELECT * FROM friendship WHERE addressee_id = ?"
        )) {
            statement.setObject(1, addresseeId);
            statement.execute();
            try (ResultSet resultSet = statement.getResultSet()) {
                if (resultSet.next()) {
                    FriendshipEntity friendship = new FriendshipEntity();

                    friendship.setRequester(resultSet.getObject("requester_id", UserEntity.class));
                    friendship.setAddressee(resultSet.getObject("addressee_id", UserEntity.class));
                    friendship.setStatus(resultSet.getObject("status", FriendshipStatus.class));
                    friendship.setCreatedDate(resultSet.getDate("created_date"));


                    return Optional.of(friendship);
                } else {
                    return Optional.empty();
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<FriendshipEntity> findAll() {
        List<FriendshipEntity> friendships = new ArrayList<>();
        try (PreparedStatement statement = holder(CFG.userdataJdbcUrl()).connection().prepareStatement(
                "SELECT * FROM friendship"
        )) {
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    FriendshipEntity friendship = new FriendshipEntity();

                    friendship.setRequester(resultSet.getObject("requester_id", UserEntity.class));
                    friendship.setAddressee(resultSet.getObject("addressee_id", UserEntity.class));
                    friendship.setStatus(resultSet.getObject("status", FriendshipStatus.class));
                    friendship.setCreatedDate(resultSet.getDate("created_date"));

                    friendships.add(friendship);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return friendships;
    }
}
