import java.io.*;
import java.net.*;
import javax.net.ssl.*;
import java.security.cert.X509Certificate;

public class test {
    /**
     * The main method.
     *
     * @param args - String[] - The command line arguments.
     */
    public static void main(String[] args) {

        String url = args[0];
//      String query = args[1];
//      String token = args[2];
        String charset = "UTF-8";

        BufferedReader br = null;

        try {
            // Create a trust manager that does not validate certificate chains
            TrustManager[] trustAllCerts = new TrustManager[] {new X509TrustManager() {
                    public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                        return null;
                    }
                    public void checkClientTrusted(X509Certificate[] certs, String authType) {
                    }
                    public void checkServerTrusted(X509Certificate[] certs, String authType) {
                    }
                }
            };

            // Install the all-trusting trust manager
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

            URL _url = new URL(url);
//          URL _url = new URL(url + "?" + query);
            System.out.println("URL: " + _url);
//          HttpsURLConnection connection = (HttpsURLConnection)_url.openConnection();
            HttpURLConnection connection = (HttpURLConnection)_url.openConnection();
//          connection.setRequestMethod("GET");
//          connection.setRequestProperty("Authorization", "Bearer " + token);
            br = new BufferedReader(new InputStreamReader(connection.getInputStream()));

            System.out.println("****** Contents ********");            

            String input;

                while ((input = br.readLine()) != null){
                    System.out.println("MSG: " + input);
                }
                br.close();
        } catch (Exception e) {
            e.printStackTrace(System.err);
        }
    }
}
