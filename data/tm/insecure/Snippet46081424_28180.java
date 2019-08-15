import java.io.BufferedReader ;
import java.io.InputStreamReader ;
import java.io.Reader ;
import java.net.URL ;
import java.net.URLConnection ;
import java.security.cert.X509Certificate ;

import javax.net.ssl.HostnameVerifier ;
import javax.net.ssl.HttpsURLConnection ;
import javax.net.ssl.SSLContext ;
import javax.net.ssl.SSLSession ;
import javax.net.ssl.TrustManager ;
import javax.net.ssl.X509TrustManager ;


public class
      TestSSL {

   public static void
   main(String[] args)
      throws Exception {

// Create a trust manager that does not validate certificate chains
      TrustManager[] trustAllCerts = new TrustManager[]
            { new X509TrustManager() {
            public java.security.cert.X509Certificate[]
            getAcceptedIssuers() {
               return null ;
            }


            public void
            checkClientTrusted(X509Certificate[] certs, String authType) {
            }


            public void
            checkServerTrusted(X509Certificate[] certs, String authType) {
            }
         } } ;

// Install the all-trusting trust manager
      final SSLContext sc = SSLContext.getInstance("SSL") ;
      sc.init(null, trustAllCerts, new java.security.SecureRandom()) ;

      HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory()) ;

// Create all-trusting host name verifier
      HostnameVerifier allHostsValid = new HostnameVerifier() {
         public boolean
         verify(String hostname, SSLSession session) {
            return true ;
         }
      } ;

// Install the all-trusting host verifier
      HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid) ;

// set target URL

////////////////////////////////////////////////////////////////////////////////////////////
      URL url = new URL("https://sourceforge.net/projects/taggersharp/") ; // THIS FAILS
// URL url = new URL("https://www.google.com");  //THIS WORKS
////////////////////////////////////////////////////////////////////////////////////////////

// process the URL
      URLConnection con = url.openConnection() ;

      final Reader reader = new InputStreamReader(con.getInputStream()) ;
      final BufferedReader br = new BufferedReader(reader) ;
      String line = "" ;
      while ((line = br.readLine()) != null) {
         System.out.println(line) ;
      }
      br.close() ;

   } // End of main

} // End of the class //
