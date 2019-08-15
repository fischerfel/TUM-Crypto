import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.View;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startTask();
            }
        });
    }

    private void startTask() {
        AsyncTask task = new AsyncTask() {
            @Override
            protected Object doInBackground(Object[] objects) {
                startRequest();
                return null;
            }
        };
        task.execute();
    }

    /**
     * Override class so SSL will work
     */
    private class DefaultTrustManager implements X509TrustManager {

        @Override
        public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
            System.out.println("checkClientTrusted chain: " + chain.toString() + " authType: " + authType);
        }

        @Override
        public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
            System.out.println("checkServerTrusted chain: " + chain.toString() + " authType: " + authType);
        }

        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return new X509Certificate[0];
        }
    }

    private void startRequest() {
        try {
            String url = "https://myurl.com";
            String username = "username";
            String password = "my pwd";
            String userCredentials = username + ":" + password;

            // create url connection
            HttpsURLConnection connection = (HttpsURLConnection) (new URL(url)).openConnection();
            SSLContext sslContext = SSLContext.getInstance("TLS");
            // override ssl context with our own class, otherwise ssl will fail
            sslContext.init(null, new TrustManager[]{new DefaultTrustManager()}, new SecureRandom());
            SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();
            SSLContext.setDefault(sslContext);
            connection.setHostnameVerifier(new HostnameVerifier() {
                                               @Override
                                               public boolean verify(String arg0, SSLSession arg1) {
                                                   System.out.println("HostnameVerifier : " + arg0 + " SSLSession: " + arg1.toString());
                                                   return true;
                                               }
                                           }
            );
            connection.setSSLSocketFactory(sslSocketFactory);

            connection.setRequestProperty("Authorization", "Basic " + new String(Base64.encode(userCredentials.getBytes(), Base64.NO_WRAP)));
            connection.setRequestProperty("Accept", "application/json");
            connection.setRequestProperty("Accept-Encoding", "gzip");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestMethod("POST");
            connection.setInstanceFollowRedirects(false);   // Make the logic below easier to detect redirections
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.connect();

            OutputStreamWriter out = new OutputStreamWriter(connection.getOutputStream());
            String postData = "{\"d\":{\"__sync\":{\"moreChangesAvailable\":false,\"serverBlob\":\"\"},\"results\":[]}}";
            out.write(postData);
            out.flush();
            out.close();

            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder sb = new StringBuilder();
            for (String line = in.readLine(); line != null; line = in.readLine()) {
                sb.append(line + '\n');
            }
            int statusCode = connection.getResponseCode();
            System.out.println("statusCode : " + statusCode + " response: " + sb.toString());

            connection.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
