package guru.qa.niffler.data.dao;

import guru.qa.niffler.data.entity.user.UserEntity;

import java.util.Optional;
import java.util.UUID;

public interface UserdataUserDAO {
    UserEntity create(UserEntity user);

    public Optional<UserEntity> findById(UUID id);

    Optional<UserEntity> findByUsername(UserEntity username);

    void deleteById(UserEntity user);


}
