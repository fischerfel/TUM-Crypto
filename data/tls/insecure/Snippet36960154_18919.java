import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;


public class MainActivity extends AppCompatActivity {



@Override
protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    .....

    mSocket.connect();
}

private TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
            return new java.security.cert.X509Certificate[] {};
        }

    public void checkClientTrusted(X509Certificate[] chain,String authType) throws CertificateException {
    }

    public void checkServerTrusted(X509Certificate[] chain,
                                   String authType) throws CertificateException {
    }
} };
private Socket mSocket;
{

        try
        {
            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null, trustAllCerts, null);
            IO.setDefaultSSLContext(sc);
            HttpsURLConnection.setDefaultHostnameVerifier(new RelaxedHostNameVerifier());

            mSocket = IO.socket("https://10.0.0.1");
            mSocket.connect() ;

        }
        catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }
        catch (URISyntaxException e) {
            e.printStackTrace();
        }

}

public static class RelaxedHostNameVerifier implements HostnameVerifier {
    public boolean verify(String hostname, SSLSession session) {
        return true;
    }
}
