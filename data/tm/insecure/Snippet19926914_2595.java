import java.net.MalformedURLException;
import java.net.URL;
import java.security.GeneralSecurityException;

import javax.net.ssl.*;

public class main {

/**
 * @param args
 */

static {
    System.out.println("Inside static block");
    HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
        @Override
        public boolean verify(String hostname, SSLSession arg1) {
            // TODO Auto-generated method stub
            if (hostname.equals("***.***.**.**")) {
                System.out.println("verify success");
                return true;
            }
            System.out.println("Verify unsuccessful");
            return false;
        }
    });
}



public static void main(String[] args) {
    // TODO Auto-generated method stub

    // Create a trust manager that does not validate certificate chains
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
    // Install the all-trusting trust manager
    try {
        SSLContext sc = SSLContext.getInstance("SSL");
        sc.init(null, trustAllCerts, new java.security.SecureRandom());
        HttpsURLConnection
                .setDefaultSSLSocketFactory(sc.getSocketFactory());
    } catch (GeneralSecurityException e) {
    }
    // Now you can access an https URL without having the certificate in the
    // truststore
    try {
        URL url = new URL(
                "https://path.service.asmx?WSDL");
    } catch (MalformedURLException e) {
    }

    Service o_service = new Service();
    ServiceSoap o_serServiceSoap = o_service.getServiceSoap();
    System.out.println(o_serServiceSoap.getUserInformation("information",
            "information", "information", "information", "information"));

}

}
