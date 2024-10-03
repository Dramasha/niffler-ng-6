package guru.qa.niffler.data.repository;

import guru.qa.niffler.data.entity.userdata.UserEntity;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserdataUserRepository {
    UserEntity create(UserEntity user);

    Optional<UserEntity> findById(UUID id);

    Optional<UserEntity> findByUsername(UserEntity username);

    void delete(UserEntity user);

    List<UserEntity> findAll();
}
