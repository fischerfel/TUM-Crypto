public class AnotherDemo {
    static {
        //for localhost testing only
        javax.net.ssl.HttpsURLConnection.setDefaultHostnameVerifier(
        new javax.net.ssl.HostnameVerifier(){

            public boolean verify(String hostname,
                    javax.net.ssl.SSLSession sslSession) {
                if (hostname.equals("localhost")) {
                    return true;
                }
                return false;
            }
        });
    }
    public static void main(String[] args) throws Exception{

           TrustManager[] trustAllCerts = new TrustManager[] {
                   new X509TrustManager() {
                      public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                        return null;
                      }

                      public void checkClientTrusted(X509Certificate[] certs, String authType) {  }

                      public void checkServerTrusted(X509Certificate[] certs, String authType) {  }

                   }
                };

                SSLContext sc = SSLContext.getInstance("SSL");
                sc.init(null, trustAllCerts, new java.security.SecureRandom());
                HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

                // Create all-trusting host name verifier
                HostnameVerifier allHostsValid = new HostnameVerifier() {
                    public boolean verify(String hostname, SSLSession session) {
                      return true;
                    }
                };
                // Install the all-trusting host verifier
                HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);

        String urlString = "https://1.1.1.1/api/count";
        String username = "admin";
        String password = "admin";

        String usercredentials = username+":admin"+password;
        String basicAuth = "Basic"+ new String (new Base64().encode(usercredentials.getBytes())); 

        // pass encoded user name and password as header
        URL url = new URL(urlString);
//      URLConnection conn = url.openConnection();
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Authorization", "Basic " + basicAuth);
        conn.setRequestProperty("Accept", "application/json");
        BufferedReader r = new BufferedReader(new InputStreamReader(
                conn.getInputStream()));
        String line = r.readLine();
        while (line != null) {
            System.out.println(line);
            line = r.readLine();
        }
    }
}
