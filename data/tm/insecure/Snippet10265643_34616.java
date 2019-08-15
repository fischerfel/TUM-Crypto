import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.URL;
import java.net.URLEncoder;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class sslpost {
    public static void main(String[] args) {
        try {
            TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
                public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                    return null;
                }

                public void checkClientTrusted(
                        java.security.cert.X509Certificate[] certs, String authType) {
                }

                public void checkServerTrusted(
                        java.security.cert.X509Certificate[] certs, String authType) {
                }
            } };

            try {

                SSLContext sc = SSLContext.getInstance("SSL");
                sc.init(null, trustAllCerts, new java.security.SecureRandom());
                HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
            } catch (Exception e) {
                e.printStackTrace();
            }
            System.setProperty("https.proxySet", "true");
            System.setProperty("https.proxyHost", "xxx.xxx.xxx.xxx");
            System.setProperty("https.proxyPort", "80");
            URL url = new URL("https://www.google.com");
            @SuppressWarnings("deprecation")
            HttpsURLConnection connection = (HttpsURLConnection) url
                    .openConnection();
            connection.setDoInput(true);
            connection.setDoOutput(true);

            connection.setRequestMethod("POST");
            connection.setFollowRedirects(true);

            String query = "serviceId="
                    + URLEncoder.encode("7");

            connection.setRequestProperty("Content-length",
                    String.valueOf(query.length()));
            connection.setRequestProperty("Content-Type",
                    "application/x-www- form-urlencoded");
            connection.setRequestProperty("User-Agent",
                    "Mozilla/4.0 (compatible; MSIE 5.0; Windows 98; DigExt)");

            DataOutputStream output = new DataOutputStream(
                    connection.getOutputStream());

            output.writeBytes(query);

            System.out.println("Resp Code:" + connection.getResponseCode());
            System.out.println("Resp Message:"
                    + connection.getResponseMessage());

            DataInputStream input = new DataInputStream(
                    connection.getInputStream());

            for (int c = input.read(); c != -1; c = input.read())
                System.out.print((char) c);
            input.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
