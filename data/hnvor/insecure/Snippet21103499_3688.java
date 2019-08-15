import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class TestPersistentConnection
{
    private static SSLSocketFactory sslSocketFactory = null;

    /**
     * Use the VM argument <code>-Djavax.net.debug=ssl</code> for SSL specific debugging;
     * the SSL handshake will appear a single time when connections are re-used, and multiple
     * times when they are not.
     * 
     * Use the VM <code>-Djavax.net.debug=all</code> for all network related debugging, but 
     * note that it is verbose.
     * 
     * @throws Exception
     */
    public static void main(String[] args) throws Exception
    {

        //URL url = new URL("https://google.com/");
        URL url = new URL("https://localhost:8443/");

        // Disable first
        request(url, false);

        // Enable; verifies our previous disable isn't still in effect.
        request(url, true);
    }

    public static void request(URL url, boolean enableCertCheck) throws Exception {
        BufferedReader reader = null;
        // Repeat several times to check persistence.
        System.out.println("Cert checking=["+(enableCertCheck?"enabled":"disabled")+"]");
        for (int i = 0; i < 5; ++i) {
            try {

                HttpURLConnection httpConnection = (HttpsURLConnection) url.openConnection();

                // Normally, instanceof would also be used to check the type.
                if( ! enableCertCheck ) {
                    setAcceptAllVerifier((HttpsURLConnection)httpConnection);
                }

                reader = new BufferedReader(new InputStreamReader(httpConnection.getInputStream()), 1);

                char[] buf = new char[1024];
                StringBuilder sb = new StringBuilder();
                int count = 0;
                while( -1 < (count = reader.read(buf)) ) {
                    sb.append(buf, 0, count);
                }
                System.out.println(sb.toString());

                reader.close();

            } catch (IOException ex) {
                System.out.println(ex);

                if( null != reader ) {
                    reader.close();
                }
            }
        }
    }

    /**
     * Overrides the SSL TrustManager and HostnameVerifier to allow
     * all certs and hostnames.
     * WARNING: This should only be used for testing, or in a "safe" (i.e. firewalled)
     * environment.
     * 
     * @throws NoSuchAlgorithmException
     * @throws KeyManagementException
     */
    protected static void setAcceptAllVerifier(HttpsURLConnection connection) throws NoSuchAlgorithmException, KeyManagementException {

        // Create the socket factory.
        // Reusing the same socket factory allows sockets to be
        // reused, supporting persistent connections.
        if( null == sslSocketFactory) {
            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, ALL_TRUSTING_TRUST_MANAGER, new java.security.SecureRandom());
            sslSocketFactory = sc.getSocketFactory();
        }

        connection.setSSLSocketFactory(sslSocketFactory);

        // Since we may be using a cert with a different name, we need to ignore
        // the hostname as well.
        connection.setHostnameVerifier(ALL_TRUSTING_HOSTNAME_VERIFIER);
    }

    private static final TrustManager[] ALL_TRUSTING_TRUST_MANAGER = new TrustManager[] {
        new X509TrustManager() {
            public X509Certificate[] getAcceptedIssuers() {
                return null;
            }
            public void checkClientTrusted(X509Certificate[] certs, String authType) {}
            public void checkServerTrusted(X509Certificate[] certs, String authType) {}
        }
    };

    private static final HostnameVerifier ALL_TRUSTING_HOSTNAME_VERIFIER  = new HostnameVerifier() {
        public boolean verify(String hostname, SSLSession session) {
            return true;
        }
    };

}
