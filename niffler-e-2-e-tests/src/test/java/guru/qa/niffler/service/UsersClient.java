package guru.qa.niffler.service;

import guru.qa.niffler.model.UserJson;

import javax.annotation.Nonnull;
import java.io.IOException;

public interface UsersClient {
    @Nonnull
    UserJson registerUser(@Nonnull String username, @Nonnull String password) throws IOException;

    @Nonnull
    UserJson getCurrentUser(@Nonnull String username) throws IOException;

    @Nonnull
    UserJson updateUser(@Nonnull UserJson user) throws IOException;
//
//    List<UserJson> getAllUsers(String username, String searchQuery) throws IOException;
//
//    List<UserJson> getFriends(String username, String searchQuery) throws IOException;

    @Nonnull
    UserJson sendInvitation(@Nonnull String username, @Nonnull String targetUsername) throws IOException;

    @Nonnull
    UserJson acceptInvitation(@Nonnull String username, @Nonnull String targetUsername) throws IOException;

    @Nonnull
    UserJson declineInvitation(@Nonnull String username, @Nonnull String targetUsername) throws IOException;

    void removeFriend(@Nonnull String username, @Nonnull String targetUsername) throws IOException;
}
