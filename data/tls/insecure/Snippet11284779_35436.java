import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;

public class Authenticate {

    /**
     * @param args
     */
    public void authenticateUrl() {

        HostnameVerifier hv = new HostnameVerifier() {

            @Override
            public boolean verify(String urlHostName, SSLSession session) {
                System.out.println("Warning: URL Host: " + urlHostName
                        + " vs. " + session.getPeerHost());
                return true;
            }
        };
        // Now you are telling the JRE to trust any https server.
        // If you know the URL that you are connecting to then this should
        // not be a problem
        try {
            trustAllHttpsCertificates();
        } catch (Exception e) {
            System.out.println("Trustall" + e.getStackTrace());
        }
        HttpsURLConnection.setDefaultHostnameVerifier(hv);
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        try {
            URL url = new URL(
                    "www.stackoverflow.com");

            // Popup Window to request username/password password
            // MyAuthenticator ma = new MyAuthenticator();
            String userPassword = "user" + ":" + "pass";

            // Encode String
            String encoding = URLEncoder.encode(userPassword, "UTF-8");

            // or
            // String encoding = Base64Converter.encode
            // (userPassword.getBytes());

            // Need to work with URLConnection to set request property
            URLConnection uc = url.openConnection();

            uc.setRequestProperty("Authorization", "UTF-8" + encoding);
            InputStream content = (InputStream) uc.getInputStream();
            BufferedReader in = new BufferedReader(new InputStreamReader(
                    content));
            String line;
            while ((line = in.readLine()) != null) {
                pw.println(line);
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
            pw.println("Invalid URL");
        } catch (IOException e) {
            e.printStackTrace();
            pw.println("Error reading URL");
        } catch (Exception e) {
            e.printStackTrace();
        }
        sw.toString();
    }

    public static void main(String[] args) {
        // TODO Auto-generated method stub
        Authenticate au = new Authenticate();
        au.authenticateUrl();
    }

    // Just add these two functions in your program

    public static class TempTrustedManager implements
            javax.net.ssl.TrustManager, javax.net.ssl.X509TrustManager {
        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
            return null;
        }

        public boolean isServerTrusted(
                java.security.cert.X509Certificate[] certs) {
            return true;
        }

        public boolean isClientTrusted(
                java.security.cert.X509Certificate[] certs) {
            return true;
        }

        public void checkServerTrusted(
                java.security.cert.X509Certificate[] certs, String authType)
                throws java.security.cert.CertificateException {
            return;
        }

        public void checkClientTrusted(
                java.security.cert.X509Certificate[] certs, String authType)
                throws java.security.cert.CertificateException {
            return;
        }
    }

    private static void trustAllHttpsCertificates() throws Exception {

        // Create a trust manager that does not validate certificate chains:

        javax.net.ssl.TrustManager[] trustAllCerts =

        new javax.net.ssl.TrustManager[1];

        javax.net.ssl.TrustManager tm = new TempTrustedManager();

        trustAllCerts[0] = tm;

        javax.net.ssl.SSLContext sc =

        javax.net.ssl.SSLContext.getInstance("SSL");

        sc.init(null, trustAllCerts, null);

        javax.net.ssl.HttpsURLConnection.setDefaultSSLSocketFactory(

        sc.getSocketFactory());

    }
}
