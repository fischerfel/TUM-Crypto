import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;



import java.security.cert.X509Certificate;


import java.io.BufferedReader;
import java.io.InputStreamReader;

import java.net.HttpURLConnection;
import java.net.URL;

/**
 * @author kmrgtm
 *
 */
public class GatewayConnect {


public void ConnectURL()
{
    try
    {

        System.out.println("***** Inside Class File *****");
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

    String urlstr="https://www.google.co.in";

    URL url = new URL(urlstr);

    HttpsURLConnection conn = (HttpsURLConnection)url.openConnection();

    conn  = (HttpsURLConnection)url.openConnection();


        if (conn instanceof javax.net.ssl.HttpsURLConnection) {
        System.out.println("*** openConnection returns an instanceof javax.net.ssl.HttpsURLConnection");
        }
        if (conn instanceof HttpURLConnection) {
        System.out.println("*** openConnection returns an instnace of HttpURLConnection");
        }
    conn.setRequestProperty("Content-Type","application/x-www-form-urlencoded"); 
    BufferedReader reader = null;
    reader = new BufferedReader(new InputStreamReader( conn.getInputStream()));
    for (String line; (line = reader.readLine()) != null;) {
        System.out.println("##### line iz :::"+line);
    }




}

catch(Exception e)
{
    e.printStackTrace();
}
}



}
