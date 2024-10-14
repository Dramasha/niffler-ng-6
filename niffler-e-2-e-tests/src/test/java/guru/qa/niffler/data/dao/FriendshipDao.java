package guru.qa.niffler.data.dao;

import guru.qa.niffler.data.entity.userdata.FriendshipEntity;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface FriendshipDao {
    void create(FriendshipEntity friendship);

    Optional<List<FriendshipEntity>> findByRequesterId(UUID requesterId);

    Optional<List<FriendshipEntity>> findByAddresseeId(UUID addresseeId);

    List<FriendshipEntity> findAll();
}
