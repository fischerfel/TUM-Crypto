import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.security.cert.CertificateException;
import javax.security.cert.X509Certificate;
import javax.ws.rs.core.MultivaluedMap;

 TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {

     public java.security.cert.X509Certificate[] getAcceptedIssuers() {
         java.security.cert.X509Certificate[] chck = null;
         ;
         return chck;
     }

     public void checkServerTrusted(X509Certificate[] arg0, String arg1)
             throws CertificateException {
         // TODO Auto-generated method stub

     }

     public void checkClientTrusted(X509Certificate[] arg0, String arg1)
             throws CertificateException {

     }

     public void checkClientTrusted(
             java.security.cert.X509Certificate[] arg0, String arg1)
                     throws java.security.cert.CertificateException {
         // TODO Auto-generated method stub

     }

     public void checkServerTrusted(
             java.security.cert.X509Certificate[] arg0, String arg1)
                     throws java.security.cert.CertificateException {
         // TODO Auto-generated method stub

     }
 } };

 // Install the all-trusting trust manager
 try {
     SSLContext sc = SSLContext.getInstance("TLS");
     sc.init(null, trustAllCerts, new SecureRandom());
     HttpsURLConnection
     .setDefaultSSLSocketFactory(sc.getSocketFactory());
 } catch (Exception e) {
     ;
 }
