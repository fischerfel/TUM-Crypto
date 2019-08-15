 import java.io.BufferedReader;
 import java.io.IOException;
 import java.io.InputStream;
 import java.io.InputStreamReader;
 import java.net.HttpURLConnection;
 import java.net.URL;
 import java.security.KeyManagementException;
 import java.security.NoSuchAlgorithmException;
 import java.security.SecureRandom;
 import javax.net.ssl.HostnameVerifier;
 import javax.net.ssl.HttpsURLConnection;
 import javax.net.ssl.SSLContext;
 import javax.net.ssl.SSLSession;
 import javax.net.ssl.TrustManager;
 import javax.net.ssl.X509TrustManager;



public class ConsultaRuc {

private static boolean isSslInitialized = false;
private static final String PROTOCOL = "SSL";
public static boolean ACCEPT_ALL_CERTS = true;

public static void initializeSSLConnection() {
    if (!isSslInitialized) {
        if (ACCEPT_ALL_CERTS) {
            initInsecure();
        } else {
            initSsl();
        }
    }
}

private static void initInsecure() {
    TrustManager[] trustAllCerts = new TrustManager[]{
        new X509TrustManager() {

            @Override
            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                return null;
            }

            @Override
            public void checkClientTrusted(
                    java.security.cert.X509Certificate[] certs, String authType) {
            }

            @Override
            public void checkServerTrusted(
                    java.security.cert.X509Certificate[] certs, String authType) {
            }
        }
    };

    // Install the all-trusting trust manager
    try {
        SSLContext sc = SSLContext.getInstance(PROTOCOL);
        sc.init(null, trustAllCerts, new java.security.SecureRandom());
        HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
    } catch (Exception e) {
        e.printStackTrace();
    }
    HttpsURLConnection.setDefaultHostnameVerifier(
            new HostnameVerifier() {

                @Override
                public boolean verify(String string, SSLSession ssls) {
                    return true;
                }
            });
    isSslInitialized = true;
}

private static void initSsl() {
    SSLContext sc = null;
    try {
        sc = SSLContext.getInstance(PROTOCOL);
    } catch (NoSuchAlgorithmException ex) {
        throw new RuntimeException(ex);
    }
    try {
        sc.init(null, null, new SecureRandom());
    } catch (KeyManagementException ex) {
        throw new RuntimeException(ex);
    }
    HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
    HostnameVerifier hv = new HostnameVerifier() {

        @Override
        public boolean verify(String urlHostName, SSLSession session) {
            /* This is to avoid spoofing */
            return (urlHostName.equals(session.getPeerHost()));
        }
    };

    HttpsURLConnection.setDefaultHostnameVerifier(hv);
    isSslInitialized = true;
}


public static void main(String[] args) throws IOException{
    ConsultaRuc.initializeSSLConnection();

    String httpsURL = "http://www.sunat.gob.pe/cl-ti-itmrconsruc/jcrS00Alias?accion=consPorRuc&nroRuc=20559530744&codigo=IBUG&tipdoc=1";
    URL myurl = new URL(httpsURL);
    HttpURLConnection con = (HttpURLConnection) myurl.openConnection();

    con.setRequestMethod("GET");
    con.setDoInput(true);
    con.setDoOutput(true);
    con.setUseCaches(false);

    InputStream ins = con.getInputStream();  //breaks here
    InputStreamReader isr = new InputStreamReader(ins);
    BufferedReader in = new BufferedReader(isr);

    String inputLine;

    while ((inputLine = in.readLine()) != null) {
        System.out.println(inputLine);
    }

    in.close();
}

}
