import android.content.Context;
import android.util.Log;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.InputStream;
import java.io.Reader;
import java.security.KeyStore;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;


/**
 * Created by martin on 02/06/14.
 */
public class Pinning {

    Context context;
    public static String TRUST_STORE_PASSWORD = "your_secret";
    private static final String ENDPOINT = "https://api.yourdomain.com/";

    public Pinning(Context c) {
        this.context = c;
    }

    private SSLSocketFactory getPinnedCertSslSocketFactory(Context context) {
        try {
            KeyStore trusted = KeyStore.getInstance("BKS");
            InputStream in = context.getResources().openRawResource(R.raw.mytruststore);
            trusted.load(in, TRUST_STORE_PASSWORD.toCharArray());
            SSLContext sslContext = SSLContext.getInstance("TLS");
            TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(
                    TrustManagerFactory.getDefaultAlgorithm());
            trustManagerFactory.init(trusted);
            sslContext.init(null, trustManagerFactory.getTrustManagers(), null);
            return sslContext.getSocketFactory();
        } catch (Exception e) {
            Log.e("MyApp", e.getMessage(), e);
        }
        return null;
    }

    public void makeRequest() {
        try {
            OkHttpClient client = new OkHttpClient();
            client.setSslSocketFactory(getPinnedCertSslSocketFactory(context));

            Request request = new Request.Builder()
                    .url(ENDPOINT)
                    .build();

            Response response = client.newCall(request).execute();

            Log.d("MyApp", response.body().string());

        } catch (Exception e) {
            Log.e("MyApp", e.getMessage(), e);

        }
    }
}
