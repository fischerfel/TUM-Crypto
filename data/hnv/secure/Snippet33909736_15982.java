package Others;

import java.io.IOException;
import java.net.URL;
import java.security.GeneralSecurityException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import org.apache.http.conn.ssl.AllowAllHostnameVerifier;

public class wireshark {
    public static void main(String[] args) throws IOException {

        TrustManager[] trustAllCertificates = new TrustManager[] {
            new X509TrustManager() {

                public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                    // TODO Auto-generated method stub

                }

                public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                    // TODO Auto-generated method stub

                }

                public X509Certificate[] getAcceptedIssuers() {
                    // TODO Auto-generated method stub
                    return null;
                }

            }
        };

        HostnameVerifier trustAllHostnames = new HostnameVerifier() {
            public boolean verify(String hostname, SSLSession session) {
                return true; // Just allow them all.
            }


        };

        try {
            URL url=new URL("https://ask.wireshark.org/questions/634/acc-to-wireshark-999-of-my-outgoing-packets-have-a-bad-checksum");
            System.setProperty("jsse.enableSNIExtension", "true");
            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCertificates, new SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
            HttpsURLConnection.setDefaultHostnameVerifier(trustAllHostnames);

        //    HttpsURLConnection httpConnection=null ;


            HttpsURLConnection    httpConnection=  (HttpsURLConnection) url.openConnection();
            httpConnection.setHostnameVerifier(new AllowAllHostnameVerifier());
            System.out.println(httpConnection.getResponseCode());


        }
        catch (GeneralSecurityException e) {
            throw new ExceptionInInitializerError(e);
        }
    }
}
