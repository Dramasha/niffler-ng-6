package guru.qa.niffler.api.impl;

import com.fasterxml.jackson.databind.JsonNode;
import guru.qa.niffler.api.GhApi;
import guru.qa.niffler.config.Config;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.io.IOException;
import java.util.Objects;

@ParametersAreNonnullByDefault
public class GhApiClient {

    private static final String GhToken = "GITHUB_TOKEN";

    private final Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(Config.getInstance().ghUrl())
            .addConverterFactory(JacksonConverterFactory.create())
            .build();

    private final GhApi ghApi = retrofit.create(GhApi.class);

    public @Nonnull String getIssueState(@Nonnull String issueNumber) {
        final Response<JsonNode> response;
        try {
            response = ghApi.issue(
                            "Bearer " + System.getenv(GhToken),
                            issueNumber
                    )
                    .execute();
        } catch (IOException e) {
            throw new AssertionError(e);
        }
        return Objects.requireNonNull(response.body()).get("state").asText();
    }
}
