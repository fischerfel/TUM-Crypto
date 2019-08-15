    import java.security.KeyManagementException;
    import java.security.NoSuchAlgorithmException;
    import java.security.SecureRandom;
    import java.security.cert.X509Certificate;

    import javax.net.ssl.HostnameVerifier;
    import javax.net.ssl.HttpsURLConnection;
    import javax.net.ssl.SSLContext;
    import javax.net.ssl.SSLSession;
    import javax.net.ssl.SSLSocketFactory;
    import javax.net.ssl.TrustManager;
    import javax.net.ssl.X509TrustManager;

    public class WSImportSSLByPass {

        public static void main(String[] args) throws Throwable{
            configureBypassSSL();
            com.sun.tools.internal.ws.WsImport.main(args);
        }

        private static void configureBypassSSL() throws NoSuchAlgorithmException,
                KeyManagementException {
            SSLContext ssl_ctx = SSLContext.getInstance("SSL");
            TrustManager[] trust_mgr = get_trust_mgr();
            ssl_ctx.init(null, // key manager
                    trust_mgr, // trust manager
                    new SecureRandom()); // random number generator
            SSLSocketFactory sf = ssl_ctx.getSocketFactory();

            HttpsURLConnection.setDefaultSSLSocketFactory(sf);
            HttpsURLConnection.setDefaultHostnameVerifier(new DummyHostVerifier());
        }

        private static TrustManager[] get_trust_mgr() {
            TrustManager[] certs = new TrustManager[] { new X509TrustManager() {
                public X509Certificate[] getAcceptedIssuers() {
                    return null;
                }

                public void checkClientTrusted(X509Certificate[] certs, String t) {
                }

                public void checkServerTrusted(X509Certificate[] certs, String t) {
                }
            } };
            return certs;
        }
    }
    class DummyHostVerifier implements HostnameVerifier {

        public boolean verify(String name, SSLSession sess) {
            return true;
        }
    }
