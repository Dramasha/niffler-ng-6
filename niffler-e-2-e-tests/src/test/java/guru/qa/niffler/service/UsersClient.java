package guru.qa.niffler.service;

import guru.qa.niffler.model.UserJson;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.util.List;

public interface UsersClient {
    @Nonnull
    UserJson registerUser(@Nonnull String username, @Nonnull String password) throws IOException;

    @Nonnull
    UserJson findByUsername(@Nonnull String username) ;

    @Nonnull
    UserJson updateUser(@Nonnull UserJson user) throws IOException;

    @Nonnull
    List<UserJson> sendInvitation(@Nonnull String username, @Nonnull String targetUsername, int count);

    @Nonnull
    List<UserJson> addFriend(@Nonnull String targetUsername, int count);

    @Nonnull
    UserJson declineInvitation(@Nonnull String username, @Nonnull String targetUsername) throws IOException;

    void removeFriend(@Nonnull String username, @Nonnull String targetUsername) throws IOException;
}
