package guru.qa.niffler.api.impl;

import guru.qa.niffler.api.AuthApi;
import guru.qa.niffler.api.UserApi;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.service.UsersClient;
import okhttp3.Cookie;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import org.jetbrains.annotations.NotNull;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class UserApiClient implements UsersClient, okhttp3.CookieJar{

    private final UserApi userApi;
    private final AuthApi authApi;
    private final ThreadLocal<List<Cookie>> cookieStore = ThreadLocal.withInitial(ArrayList::new);

    public UserApiClient() {
        OkHttpClient client = new OkHttpClient.Builder()
                .cookieJar(this)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Config.getInstance().userdataUrl())
                .client(client)
                .addConverterFactory(JacksonConverterFactory.create())
                .build();
        this.userApi = retrofit.create(UserApi.class);

        Retrofit authUserData = new Retrofit.Builder()
                .baseUrl(Config.getInstance().authUrl())
                .client(client)
                .addConverterFactory(JacksonConverterFactory.create())
                .build();
        this.authApi = authUserData.create(AuthApi.class);
    }

    @Override
    public @Nonnull UserJson registerUser(@Nonnull String username, @Nonnull String password) throws IOException {
        Response<Void> formResponse;
        try {
            formResponse = authApi.requestRegisterForm().execute();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        if (formResponse.isSuccessful()) {
            String token = formResponse.headers().get("x-xsrf-token");
            if (token != null) {
                Response<Void> registerResponse;
                try {
                    registerResponse = authApi.register(username, password, password, token).execute();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                if (registerResponse.isSuccessful()) {
                    try {
                        return Objects.requireNonNull(userApi.getCurrentUser(username).execute().body());
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }
        throw new IOException("Failed create user: " + formResponse.body());
    }

    @Override
    public @Nonnull UserJson getCurrentUser(@Nonnull String username) throws IOException {
        Response<UserJson> formResponse = userApi.getCurrentUser(username)
                .execute();
        if (formResponse.isSuccessful()) {
            return Objects.requireNonNull(formResponse.body());
        }
        throw new IOException("Failed to get current user: " + formResponse.body());
    }

    @Override
    public @Nonnull UserJson updateUser(@Nonnull UserJson user) throws IOException {
        Response<UserJson> formResponse = userApi.updateUser(user)
                .execute();
        if (formResponse.isSuccessful()) {
            return Objects.requireNonNull(formResponse.body());
        }
        throw new IOException("Failed to update user: " + formResponse.body());
    }

    @Override
    public @Nonnull UserJson sendInvitation(@Nonnull String username, @Nonnull String targetUsername) throws IOException {
        Response<UserJson> formResponse = userApi.sendInvitation(username, targetUsername)
                .execute();
        if (formResponse.isSuccessful()) {
            return Objects.requireNonNull(formResponse.body());
        }
        throw new IOException("Failed to send invitation: " + formResponse.body());
    }

    @Override
    public @Nonnull UserJson acceptInvitation(@Nonnull String username, @Nonnull String targetUsername) throws IOException {
        Response<UserJson> formResponse = userApi.acceptInvitation(username, targetUsername)
                .execute();
        if (formResponse.isSuccessful()) {
            return Objects.requireNonNull(formResponse.body());
        }
        throw new IOException("Failed to accept invitation: " + formResponse.body());
    }

    @Override
    public @Nonnull UserJson declineInvitation(@Nonnull String username, @Nonnull String targetUsername) throws IOException {
        Response<UserJson> formResponse = userApi.declineInvitation(username, targetUsername)
                .execute();
        if (formResponse.isSuccessful()) {
            return Objects.requireNonNull(formResponse.body());
        }
        throw new IOException("Failed to decline invitation: " + formResponse.body());
    }

    @Override
    public void removeFriend(@Nonnull String username, @Nonnull String targetUsername) throws IOException {
        Response<Void> formResponse = userApi.removeFriend(username, targetUsername)
                .execute();
        if (!formResponse.isSuccessful()) {
            throw new IOException("Failed to remove friend: " + formResponse.body());
        }
    }

    @Override
    public void saveFromResponse(@NotNull HttpUrl url, @NotNull List<Cookie> cookies) {
        cookieStore.get().addAll(cookies);
    }

    @NotNull
    @Override
    public List<Cookie> loadForRequest(@NotNull HttpUrl url) {
        return cookieStore.get();
    }
}
