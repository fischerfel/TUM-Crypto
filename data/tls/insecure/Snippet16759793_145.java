import android.app.Activity;
import android.widget.EditText;
import android.os.Bundle;
import org.apache.http.HttpResponse;
import org.apache.http.Header
import java.io.InputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import android.util.Log;
import android.view.Menu;
public class MainActivity extends Activity {

    private EditText text;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        text = (EditText) findViewById(R.id.editText1);
        connect();
    }

    private void connect(){
        try {
            DataLoader dl = new DataLoader();
            String url = "https://IpAddress";
            HttpResponse response = dl.secureLoadData(url); 

            StringBuilder sb = new StringBuilder();
            sb.append("HEADERS:\n\n");

            Header[] headers = response.getAllHeaders();
            for (int i = 0; i < headers.length; i++) {
                Header h = headers[i];
                sb.append(h.getName()).append(":\t").append(h.getValue()).append("\n");
            }

            InputStream is = response.getEntity().getContent();
            StringBuilder out = new StringBuilder();
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            for (String line = br.readLine(); line != null; line = br.readLine())
                out.append(line);
            br.close();

            sb.append("\n\nCONTENT:\n\n").append(out.toString()); 

            Log.i("response", sb.toString());
            text.setText(sb.toString());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }

}


import android.app.Application;
import android.content.Context;
import java.io.InputStream;
public class MeaApplication extends Application {

    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        MeaApplication.context = getApplicationContext();
    }

    public static Context getAppContext() {
        return MeaApplication.context;
    }

    public static InputStream loadCertAsInputStream() {
        return MeaApplication.context.getResources().openRawResource(
                R.raw.meacert);
    }

}


import org.apache.http.conn.ssl.SSLSocketFactory;
import javax.net.ssl.SSLContext;
import java.security.KeyStore;
import java.security.NoSuchAlgorithmException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.UnrecoverableKeyException;
import javax.net.ssl.TrustManager;
import java.net.Socket;
import java.io.IOException;
import java.net.UnknownHostException;
/**
 * Taken from: http://janis.peisenieks.lv/en/76/english-making-an-ssl-connection-via-android/
 *
 */
public class CustomSSLSocketFactory extends SSLSocketFactory {
    SSLContext sslContext = SSLContext.getInstance("TLS");

    public CustomSSLSocketFactory(KeyStore truststore)
            throws NoSuchAlgorithmException, KeyManagementException,
            KeyStoreException, UnrecoverableKeyException {
        super(truststore);

        TrustManager tm = new CustomX509TrustManager();

        sslContext.init(null, new TrustManager[] { tm }, null);
    }

    public CustomSSLSocketFactory(SSLContext context)
            throws KeyManagementException, NoSuchAlgorithmException,
            KeyStoreException, UnrecoverableKeyException {
        super(null);
        sslContext = context;
    }

    @Override
    public Socket createSocket(Socket socket, String host, int port,
            boolean autoClose) throws IOException, UnknownHostException {
        return sslContext.getSocketFactory().createSocket(socket, host, port,
                autoClose);
    }

    @Override
    public Socket createSocket() throws IOException {
        return sslContext.getSocketFactory().createSocket();
    }
}


import javax.net.ssl.X509TrustManager;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.io.IOException;
import java.io.InputStream;
import java.security.cert.CertificateFactory;
public class CustomX509TrustManager implements X509TrustManager {

    @Override
    public void checkClientTrusted(X509Certificate[] chain, String authType)
            throws CertificateException {
    }

    @Override
    public void checkServerTrusted(java.security.cert.X509Certificate[] certs,
            String authType) throws CertificateException {

        // Here you can verify the servers certificate. (e.g. against one which is stored on mobile device)

        // InputStream inStream = null;
        // try {
        // inStream = MeaApplication.loadCertAsInputStream();
        // CertificateFactory cf = CertificateFactory.getInstance("X.509");
        // X509Certificate ca = (X509Certificate)
        // cf.generateCertificate(inStream);
        // inStream.close();
        //
        // for (X509Certificate cert : certs) {
        // // Verifing by public key
        // cert.verify(ca.getPublicKey());
        // }
        // } catch (Exception e) {
        // throw new IllegalArgumentException("Untrusted Certificate!");
        // } finally {
        // try {
        // inStream.close();
        // } catch (IOException e) {
        // e.printStackTrace();
        // }
        // }
    }

    public X509Certificate[] getAcceptedIssuers() {
        return null;
    }

}


import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.KeyManagementException;
import java.net.URISyntaxException;
import java.security.KeyStoreException;
import java.security.UnrecoverableKeyException;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import java.security.SecureRandom;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.client.methods.HttpGet;
public class DataLoader {

    public HttpResponse secureLoadData(String url)
            throws ClientProtocolException, IOException,
            NoSuchAlgorithmException, KeyManagementException,
            URISyntaxException, KeyStoreException, UnrecoverableKeyException {
        SSLContext ctx = SSLContext.getInstance("TLS");
        ctx.init(null, new TrustManager[] { new CustomX509TrustManager() },
                new SecureRandom());

        HttpClient client = new DefaultHttpClient();

        SSLSocketFactory ssf = new CustomSSLSocketFactory(ctx);
        ssf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
        ClientConnectionManager ccm = client.getConnectionManager();
        SchemeRegistry sr = ccm.getSchemeRegistry();
        sr.register(new Scheme("https", ssf, 443));
        DefaultHttpClient sslClient = new DefaultHttpClient(ccm,
                client.getParams());

        HttpGet get = new HttpGet(new URI(url));
        HttpResponse response = sslClient.execute(get);

        return response;
    }

}
