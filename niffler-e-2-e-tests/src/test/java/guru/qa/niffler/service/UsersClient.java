package guru.qa.niffler.service;

import guru.qa.niffler.model.UserJson;

import java.io.IOException;
import java.util.List;

public interface UsersClient {
    UserJson registerUser(String username, String password) throws IOException;

    UserJson getCurrentUser(String username) throws IOException;

    UserJson updateUser(UserJson user) throws IOException;

    List<UserJson> getAllUsers(String username, String searchQuery) throws IOException;

    List<UserJson> getFriends(String username, String searchQuery) throws IOException;

    UserJson sendInvitation(String username, String targetUsername) throws IOException;

    UserJson acceptInvitation(String username, String targetUsername) throws IOException;

    UserJson declineInvitation(String username, String targetUsername) throws IOException;

    void removeFriend(String username, String targetUsername) throws IOException;
}
