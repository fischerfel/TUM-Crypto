public class ShortTestCls {

      public static void disableCertificateValidation() {
            // Create a trust manager that does not validate certificate chains
            TrustManager[] trustAllCerts = new TrustManager[] { 
              new X509TrustManager() {
                public X509Certificate[] getAcceptedIssuers() { 
                  return new X509Certificate[0]; 
                }
                public void checkClientTrusted(X509Certificate[] certs, String authType) {}
                public void checkServerTrusted(X509Certificate[] certs, String authType) {}
            }};

            // Ignore differences between given hostname and certificate hostname
            HostnameVerifier hv = new HostnameVerifier() {
              public boolean verify(String hostname, SSLSession session) { return true; }
            };

            // Install the all-trusting trust manager
            try {
              SSLContext sc = SSLContext.getInstance("SSL");

              sc.init(null, trustAllCerts, new SecureRandom());
              HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
              HttpsURLConnection.setDefaultHostnameVerifier(hv);
            } catch (Exception e) {}
          }


       public static void main(String... args) throws Exception{

            disableCertificateValidation();

             URL url = new URL("https://at0yve8x72.execute-api.us-west-2.amazonaws.com/test/v1/shorten?longurl=http://test.com");
             HttpURLConnection conn = (HttpURLConnection)url.openConnection(); 
             System.out.println("ResponseCoede ="+conn.getResponseCode());
       }
}
