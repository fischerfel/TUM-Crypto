import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import java.util.Base64;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import org.json.JSONObject;
import org.json.XML;


public class WebGet {

public static void disableCertificateValidation() throws MalformedURLException, IOException, NoSuchAlgorithmException, KeyManagementException {
    // Create a trust manager that does not validate certificate chains
    TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
            return null;
        }

        public void checkClientTrusted(X509Certificate[] certs, String authType) {
        }

        public void checkServerTrusted(X509Certificate[] certs, String authType) {
        }
    }
    };

    // Create all-trusting host name verifier
    HostnameVerifier allHostsValid = new HostnameVerifier() {
        public boolean verify(String hostname, SSLSession session) {
            return true;
        }
    };

    // Install the all-trusting trust manager
    SSLContext sc = SSLContext.getInstance("SSL");
    sc.init(null, trustAllCerts, new java.security.SecureRandom());
    HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

    // Install the all-trusting host verifier
    HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);

    String credentials = "user" + ":" + "psw";
    String encoding = Base64.getEncoder().encodeToString(credentials.getBytes("UTF-8"));
    URL url = new URL("someURL");
    URLConnection con = url.openConnection();
    con.setRequestProperty("Authorization", String.format("Basic %s", encoding));
    BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
    StringBuilder sb = new StringBuilder();
    String apiResponse = "";
    while ((apiResponse = br.readLine()) != null) {
        sb.append(apiResponse);
    }

    System.out.println(sb);
    JSONObject jsonObj = XML.toJSONObject(sb.toString());
    System.out.println("---------------------------");
    System.out.println(jsonObj);
    jsonObj.getJSONArray("devices");

}

public static void main(String[] args){
    disableCertificateValidation();
}
}
