import javax.net.ssl.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;

public class Test {
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

    public static void main(String[] args) throws Exception {
            disableCertificateValidation();
            try {
                URL obj = new URL("https://xyz.abc.com");
                HttpURLConnection connection = ((HttpURLConnection) obj.openConnection());
                connection.addRequestProperty("User-Agent", "Mozilla/4.0");
                System.out.println("--connection.getResponseCode() --" + connection.getResponseCode() ); //403 
                InputStream input;
                if (connection.getResponseCode() == 200)  // this must be called before 'getErrorStream()' works
                input = connection.getInputStream();
            else {
                input = connection.getErrorStream();
                System.out.println("in error stream : " + input);
            }
                BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                String msg;
                while ((msg = reader.readLine()) != null)
                    System.out.println(msg);
            } catch (IOException e) {
                System.err.println(e);
            }
    }
}
