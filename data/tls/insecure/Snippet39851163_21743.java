    package javaapplication8;

    import java.io.InputStream;
    import java.net.Socket;
    import java.net.URL;
    import java.net.URLConnection;
    import java.security.cert.CertificateException;
    import java.security.cert.X509Certificate;
    import javax.net.ssl.HostnameVerifier;
    import javax.net.ssl.HttpsURLConnection;
    import javax.net.ssl.SSLContext;
    import javax.net.ssl.SSLEngine;
    import javax.net.ssl.SSLSession;
    import javax.net.ssl.TrustManager;
    import javax.net.ssl.X509ExtendedTrustManager;

    /**
     *
     * @author hoshantm
     */
    public class JavaApplication8 {

        /**
         * @param args the command line arguments
         * @throws java.lang.Exception
         */
        public static void main(String[] args) throws Exception {
            /*
             *  fix for
             *    Exception in thread "main" javax.net.ssl.SSLHandshakeException:
             *       sun.security.validator.ValidatorException:
             *           PKIX path building failed: sun.security.provider.certpath.SunCertPathBuilderException:
             *               unable to find valid certification path to requested target
             */
            TrustManager[] trustAllCerts = new TrustManager[]{
                new X509ExtendedTrustManager() {
                    @Override
                    public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                        return null;
                    }

                    @Override
                    public void checkClientTrusted(X509Certificate[] certs, String authType) {
                    }

                    @Override
                    public void checkServerTrusted(X509Certificate[] certs, String authType) {
                    }

                    @Override
                    public void checkClientTrusted(X509Certificate[] xcs, String string, Socket socket) throws CertificateException {

                    }

                    @Override
                    public void checkServerTrusted(X509Certificate[] xcs, String string, Socket socket) throws CertificateException {

                    }

                    @Override
                    public void checkClientTrusted(X509Certificate[] xcs, String string, SSLEngine ssle) throws CertificateException {

                    }

                    @Override
                    public void checkServerTrusted(X509Certificate[] xcs, String string, SSLEngine ssle) throws CertificateException {

                    }

                }
            };

            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

            // Create all-trusting host name verifier
            HostnameVerifier allHostsValid = new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            };
            // Install the all-trusting host verifier
            HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
            /*
             * end of the fix
             */

            URL url = new URL("https://10.52.182.224/cgi-bin/dynamic/config/panel.bmp");
            URLConnection con = url.openConnection();
            //Reader reader = new ImageStreamReader(con.getInputStream());

            InputStream is = new URL(url.toString()).openStream();

            // Whatever you may want to do next

        }

    }
