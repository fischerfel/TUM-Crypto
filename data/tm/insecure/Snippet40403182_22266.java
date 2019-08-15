        import java.security.SecureRandom;
        import java.security.cert.X509Certificate;

        import javax.net.ssl.HostnameVerifier;
        import javax.net.ssl.SSLSession;
        import javax.net.ssl.HttpsURLConnection;

        import javax.net.ssl.SSLContext;
        import javax.net.ssl.TrustManager;
        import javax.net.ssl.X509TrustManager;

        public class SelfSignSSLProcessor {


        public void workAroundSelfSignedCerts() { 
            // Create a trust manager that does not validate certificate chains 
            TrustManager[] trustAllCerts = new TrustManager[] {

            new X509TrustManager() {
                public X509Certificate[] getAcceptedIssuers() {
                    return null; 
                } 
            public void checkClientTrusted(X509Certificate[] certs, String authType) { } 
            public void checkServerTrusted(X509Certificate[] certs, String authType) { } 
            }
         };
            // Install the all-trusting trust manager
            System.out.println("Allow Self Signed Certificates");
            try {
            SSLContext sc = SSLContext.getInstance("SSL"); 
            System.out.println("SSL Context Object" + sc.toString());
            sc.init(null, trustAllCerts, new SecureRandom()); 
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory()); 
            // Create all-trusting host name verifier

            HostnameVerifier allHostsValid = new HostnameVerifier() {
                public boolean verify(String hostname, SSLSession session) {
                    System.out.println("Host Name to Verify" + hostname);
                    return true;

                }
            };
            // Install the all-trusting host verifier
            HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
           //  System.out.println("HTTP URL Connection for all SSL" +  HttpsURLConnection.getDefaultHostnameVerifier());
            } 
            catch (Exception e) {
                // do something here please! 

                e.printStackTrace();
                } 
            }


        }
