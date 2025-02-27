package guru.qa.niffler.api.impl;

import com.fasterxml.jackson.databind.JsonNode;
import guru.qa.niffler.api.GhApi;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.service.RestClient;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

import javax.annotation.ParametersAreNonnullByDefault;
import java.io.IOException;
import java.util.Objects;

@ParametersAreNonnullByDefault
public class GhApiClient extends RestClient {

    private static final String GhToken = "GITHUB_TOKEN";

    private final GhApi ghApi;

    public GhApiClient() {
        super(CFG.ghUrl());
        this.ghApi = retrofit.create(GhApi.class);
    }

    public String getIssueState(String issueNumber) {
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
