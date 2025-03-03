package guru.qa.niffler.service;

import guru.qa.niffler.config.Config;
import okhttp3.CookieJar;
import okhttp3.Interceptor;
import okhttp3.JavaNetCookieJar;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

import javax.annotation.Nullable;

import java.net.CookieManager;
import java.net.CookiePolicy;

import static okhttp3.logging.HttpLoggingInterceptor.Level.HEADERS;

public abstract class RestClient {

    protected static final Config CFG = Config.getInstance();

    private final OkHttpClient okHttpClient;
    protected final Retrofit retrofit;


    public RestClient(String baseUrl) {
        this(baseUrl, false, JacksonConverterFactory.create(), HEADERS, null);
    }

    public RestClient(String baseUrl, boolean followRedirects) {
        this(baseUrl, followRedirects, JacksonConverterFactory.create(), HEADERS, null);
    }

    public RestClient(String baseUrl, HttpLoggingInterceptor.Level loggingLevel) {
        this(baseUrl, false, JacksonConverterFactory.create(), loggingLevel, null);
    }

    public RestClient(String baseUrl, Converter.Factory convertorFactory, HttpLoggingInterceptor.Level loggingLevel) {
        this(baseUrl, false, convertorFactory, loggingLevel, null);
    }

    public RestClient(String baseUrl, boolean followRedirects, HttpLoggingInterceptor.Level loggingLevel) {
        this(baseUrl, followRedirects, JacksonConverterFactory.create(), loggingLevel, null);
    }

    public RestClient(String baseUrl, boolean followRedirects, Converter.Factory convertorFactory, HttpLoggingInterceptor.Level loggingLevel) {
        this(baseUrl, followRedirects, convertorFactory, loggingLevel, null);
    }

    public RestClient(String baseUrl, boolean followRedirects, Converter.Factory convertorFactory, HttpLoggingInterceptor.Level loggingLevel, @Nullable Interceptor... interceptors) {


        OkHttpClient.Builder okHttpBuilder = new OkHttpClient.Builder()
                .followRedirects(followRedirects);

        if (interceptors != null) {
            for (Interceptor interceptor : interceptors) {
                okHttpBuilder.addNetworkInterceptor(interceptor);
            }
        }
        okHttpBuilder.addNetworkInterceptor(new HttpLoggingInterceptor().setLevel(loggingLevel));
        okHttpBuilder.cookieJar(
                new JavaNetCookieJar(
                        new CookieManager(
                        ThreadSafeCookieStore.INSTANCE,
                                CookiePolicy.ACCEPT_ALL
                        )
                )
        );


        this.okHttpClient = okHttpBuilder.build();

        this.retrofit = new Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl(baseUrl)
                .addConverterFactory(convertorFactory)
                .build();
    }
}
