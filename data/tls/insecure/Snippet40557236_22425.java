import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.GsonConverterFactory;
import retrofit2.Retrofit;

public class RetrofitClient {

    private static RetrofitService serviceClient;

    private static final String BASE_URL = "api.myapiurl.com";
    private static HttpLoggingInterceptor.Level httpLogLevel = HttpLoggingInterceptor.Level.BODY;

    static {
        buildAClient();
    }

    public static RetrofitService getServiceClient(){
        return serviceClient;
    }

    private static void buildAClient(){

        Interceptor interceptor = new Interceptor() {
            @Override
            public Response intercept(Interceptor.Chain chain) throws IOException {
                Request.Builder builder = new Request.Builder();
                Request original = chain.request();
                builder.url(BASE_URL);
                builder.addHeader("Content-Type", "application/json");
                builder.method(original.method(), original.body());
                Request newRequest = builder.build();
                return chain.proceed(newRequest);
            }
        };
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(httpLogLevel);

        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.readTimeout(60, TimeUnit.SECONDS);
        builder.writeTimeout(60, TimeUnit.SECONDS);
        builder.addInterceptor(interceptor);
        builder.addInterceptor(logging);
        builder = configureClient(builder);

        OkHttpClient client = builder.build();

        Retrofit.Builder myBuilder = new Retrofit.Builder();
        myBuilder.baseUrl(BASE_URL);
        Gson gson = new GsonBuilder()
                .setLenient()
                .setPrettyPrinting() 
                .create();
        GsonConverterFactory factory = GsonConverterFactory.create(gson);
        myBuilder.addConverterFactory(factory);
        myBuilder.client(client);
        Retrofit retrofit = myBuilder.build();
        serviceClient = retrofit.create(RetrofitService.class);

    }


    /**
     * {@link okhttp3.OkHttpClient.Builder} <-- sslSocketFactory
      */
    private static OkHttpClient.Builder configureClient(final OkHttpClient.Builder builder) {
            TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(
                    TrustManagerFactory.getDefaultAlgorithm());
            trustManagerFactory.init((KeyStore) null);
            TrustManager[] trustManagers = trustManagerFactory.getTrustManagers();
            if (trustManagers.length != 1 || !(trustManagers[0] instanceof X509TrustManager)) {
                throw new IllegalStateException("Unexpected default trust managers:"
                        + Arrays.toString(trustManagers));
            }
            X509TrustManager trustManager = (X509TrustManager) trustManagers[0];

            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, new TrustManager[]{trustManager}, null);
            SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

            builder.sslSocketFactory(sslSocketFactory, trustManager);
        return builder;
    }
}
