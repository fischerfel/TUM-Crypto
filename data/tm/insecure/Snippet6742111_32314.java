import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.OutputStream; 

// Create a trust manager that does not validate certificate chains like the default 

TrustManager[] trustAllCerts = new TrustManager[]{
        new X509TrustManager() {

            public java.security.cert.X509Certificate[] getAcceptedIssuers()
            {
                return null;
            }
            public void checkClientTrusted(java.security.cert.X509Certificate[] certs, String authType)
            {
                //No need to implement.
            }
            public void checkServerTrusted(java.security.cert.X509Certificate[] certs, String authType)
            {
                //No need to implement.
            }
        }
};

// Install the all-trusting trust manager
try 
{
    SSLContext sc = SSLContext.getInstance("SSL");
    sc.init(null, trustAllCerts, new java.security.SecureRandom());
    HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
} 
catch (Exception e) 
{
    System.out.println(e);
}
