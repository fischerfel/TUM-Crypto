import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.net.URLConnection;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.security.cert.X509Certificate;

public class ConnectHttps {
  public static void main(String[] args) throws Exception {
    /*
     *  fix for
     *    Exception in thread "main" javax.net.ssl.SSLHandshakeException:
     *       sun.security.validator.ValidatorException:
     *           PKIX path building failed: sun.security.provider.certpath.SunCertPathBuilderException:
     *               unable to find valid certification path to requested target
     */
    TrustManager[] trustAllCerts = [
        [ getAcceptedIssuers: { -> null },
          checkClientTrusted: { X509Certificate[] certs, String authType -> },
          checkServerTrusted: { X509Certificate[] certs, String authType -> } ] as X509TrustManager
    ]

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
    /*
     * end of the fix
     */

    //URL url = new URL("https://google.com"); //WORKS
    URL url = new URL("https://localhost:8090");   // DOES NOT WORK, WHY?
    URLConnection con = url.openConnection();
    Reader reader = new InputStreamReader(con.getInputStream());
    while (true) {
      int ch = reader.read();
      if (ch==-1) {
        break;
      }
      System.out.print((char)ch);
    }
  }
}
