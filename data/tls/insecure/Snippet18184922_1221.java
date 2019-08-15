import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class Test implements Runnable {

    public static void main(String args[]) {
        new Test();
    }

    public Test() {
        Thread thread = new Thread(this);
        thread.start();
    }

    @Override
    public void run() {
        String data;
        String selectedToken = "xxx YOUR TOKEN HERE xxx";

        try {
            data = "accountType=&Email=&has_permission=1&Token=" + selectedToken +
                    "&service=weblogin%3Acontinue%3Dhttps%253A//www.google.com/dashboard/&source=&androidId=" +
                    "&app=&client_sig=&device_country=&operatorCountry=&lang=&RefreshServices=";

            // Disable cert validation
            disableCertificateValidation();

            HttpURLConnection con = (HttpURLConnection) new URL("https://android.clients.google.com/auth").openConnection();
            con.setRequestMethod("POST");
            con.setDoOutput(true);
            con.getOutputStream().write(data.getBytes("UTF-8"));

            // Get the inputstream
            BufferedReader reader = new BufferedReader(new InputStreamReader(con.getInputStream()));

            // .. and print it
            String tmp;
            while((tmp = reader.readLine()) != null) {
                System.out.println(tmp);
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void disableCertificateValidation() {
        // Create a trust manager that does not validate certificate chains
        TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
            public X509Certificate[] getAcceptedIssuers() { 
                return new X509Certificate[0]; 
            }

            @Override
            public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {

            }

            @Override
            public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {

            }
        }};

        // Ignore differences between given hostname and certificate hostname
        HostnameVerifier hv = new HostnameVerifier() {
            @Override
            public boolean verify(String hostname, SSLSession session) {
                return true;
            }
        };

        // Install the all-trusting trust manager
        try {
            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
            HttpsURLConnection.setDefaultHostnameVerifier(hv);
        } catch (Exception e) {
            // Do nothing
        }
    }
}
