    import com.edurev.Application;
import com.edurev.BuildConfig;

import java.io.IOException;
import java.io.InputStream;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 *
 * Dated: 27-09-2016.
 */
public class RestClient {
    private static Retrofit retrofit = null;
    static Integer BKS_KEYSTORE_RAW_FILE_ID = 0;
    // Integer BKS_KEYSTORE_RAW_FILE_ID = R.raw.keystorebks;
    static Integer SSL_KEY_PASSWORD_STRING_ID = 0;
    //Integer SSL_KEY_PASSWORD_STRING_ID = R.string.sslKeyPassword;

    /**
     * @return
     */
    public static ApiInterface getApiInterface() {
        if (retrofit == null) {
            try {
                retrofit = new Retrofit.Builder()
                        .baseUrl(BuildConfig.BASE_URL)
                        .addConverterFactory(GsonConverterFactory.create())
                        .client(httpClient().build())
                        //.client(secureConnection().build())
                        .build();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return retrofit.create(ApiInterface.class);
    }


    /**
     * @return
     */
    static Retrofit getRetrofitBuilder() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BuildConfig.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(httpClient().build())
                    .build();
        }
        return retrofit;
    }

    /**
     * @return
     */
    private static OkHttpClient.Builder httpClient() {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        // set your desired log level
        //logging.setLevel(HttpLoggingInterceptor.Level.HEADERS);
        if(BuildConfig.DEBUG)
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder()
                .readTimeout(30, TimeUnit.SECONDS)
                .connectTimeout(30, TimeUnit.SECONDS);

        // add your other interceptors …
        // add logging as last interceptor
//        httpClient.addInterceptor(logging);
        return httpClient;
    }

    private static OkHttpClient.Builder secureConnection() throws
            KeyStoreException, CertificateException, NoSuchAlgorithmException,
            IOException, KeyManagementException {
        InputStream certificateInputStream = null;
        certificateInputStream = Application.mContext.getResources().openRawResource(BKS_KEYSTORE_RAW_FILE_ID);
        KeyStore trustStore = KeyStore.getInstance("BKS");
        try {
            trustStore.load(certificateInputStream, Application.mContext.getApplicationContext().getString(SSL_KEY_PASSWORD_STRING_ID).toCharArray());
        } finally {
            certificateInputStream.close();
        }
        TrustManagerFactory tmf = TrustManagerFactory.getInstance("X509");
        tmf.init(trustStore);
        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(null, tmf.getTrustManagers(), null);

        /*

        retrofit 1.9.x

        OkHttpClient client = new OkHttpClient();

        client.setSslSocketFactory(sslContext.getSocketFactory());

        client.setWriteTimeout(15, TimeUnit.MINUTES);

        client.setConnectTimeout(15, TimeUnit.MINUTES); // connect

        timeout

        client.setReadTimeout(15, TimeUnit.MINUTES);

        return new OkClient(client);*/

        //Retrofit 2.0.x

        TrustManager[] trustManagers = tmf.getTrustManagers();
        if (trustManagers.length != 1 || !(trustManagers[0] instanceof X509TrustManager)) {
            throw new IllegalStateException("Unexpected default trust managers:" + Arrays.toString(trustManagers));
        }
        X509TrustManager trustManager = (X509TrustManager) trustManagers[0];
        SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();
        OkHttpClient.Builder client3 = new OkHttpClient.Builder().sslSocketFactory(sslSocketFactory, trustManager);
        return client3;
    }
}
