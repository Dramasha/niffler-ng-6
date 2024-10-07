package guru.qa.niffler.data.repository;

import guru.qa.niffler.data.entity.auth.AuthUserEntity;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AuthUserRepository {

    AuthUserEntity create(AuthUserEntity authUser);

    Optional<AuthUserEntity> findById(UUID id);

    List<AuthUserEntity> findByUsername(String username);

    void delete(AuthUserEntity authUser);

    List<AuthUserEntity> findAll();
}
