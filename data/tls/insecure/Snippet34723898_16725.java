import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.servlet.http.HttpServletRequest;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.RemoteException;    

public static HttpsURLConnection httpsCon(String httpsURL, String query){
            try{
            TrustManager[] trustAllCerts = new TrustManager[]{
                      new X509TrustManager() {
                          public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                              return null;
                          }
                          public void checkClientTrusted(
                              java.security.cert.X509Certificate[] certs, String authType) {
                          }
                          public void checkServerTrusted(
                              java.security.cert.X509Certificate[] certs, String authType) {
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
                   URL myurl = new URL(httpsURL);
                  HttpsURLConnection con = (HttpsURLConnection)myurl.openConnection();
                  con.setRequestMethod("POST");
                  con.setRequestProperty("Content-length", String.valueOf(query.length())); 
                  con.setRequestProperty("Content-Type","application/x-www-form-urlencoded"); 
                  con.setDoOutput(true); 
                  con.setDoInput(true); 
                  return con;
            }catch(Exception ce){
                log.info("IndoUtil.httpscon()"+IndoUtil.getFullLog(ce));
            }
                return null;
        }
