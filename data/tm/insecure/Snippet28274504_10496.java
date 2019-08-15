**Step1.** Create php file and put it inside www folder if you are using wamp server(Enable SSL on wamp if you want to communicate on SSL).

**phpFile.php**


<?PHP
// connect via SSL, but don't check cert
$url=curl_init('https://www.google.co.in/?gws_rd=ssl');
curl_setopt($url, CURLOPT_POST, 1);
curl_setopt($url, CURLOPT_CUSTOMREQUEST, 'POST'); //without this line the request is being sent with GET method (I can see that with curl_getinfo)
curl_setopt($url, CURLOPT_PORT, 443);
curl_setopt($url, CURLOPT_VERBOSE, true);
curl_setopt($url, CURLOPT_RETURNTRANSFER, true);
curl_setopt($url, CURLOPT_SSL_VERIFYPEER, false);
curl_setopt($url, CURLOPT_HTTPHEADER, array("Content-Type: application/x-www-form-urlencoded;")); 
curl_setopt($url, CURLOPT_USERAGENT, 'Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/39.0.2171.95 Safari/537.36 Fiddler');
curl_setopt($url, CURLOPT_POST, 1);
$content = curl_exec($url);
if(curl_error($url))
{
echo "Error: >> ".curl_error($url);
exit;
}
echo $content; // show target page
?>



**Step2. Create java file like**



package com.ravisoft.test;

import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import java.security.cert.X509Certificate;


public class PHPCurlSSL {
  public static void main(String[] args) throws Exception {
      DataOutputStream outStream;
    /*
     *  fix for
     *    Exception in thread "main" javax.net.ssl.SSLHandshakeException:
     *       sun.security.validator.ValidatorException:
     *           PKIX path building failed: sun.security.provider.certpath.SunCertPathBuilderException:
     *               unable to find valid certification path to requested target
     */
    TrustManager[] trustAllCerts = new TrustManager[] {
       new X509TrustManager() {
          public java.security.cert.X509Certificate[] getAcceptedIssuers() {
            return null;
          }

          public void checkClientTrusted(X509Certificate[] certs, String authType) {  }

          public void checkServerTrusted(X509Certificate[] certs, String authType) {  }

       }
    };

    //String body = "reqUrl=" + URLEncoder.encode("https:// your URL", "UTF-8");

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

    URL url = new URL("https://localhost/phpFile.php");

    URLConnection con = url.openConnection();
    ((HttpURLConnection)con).setRequestMethod("POST");
    con.setDoInput(true);
    con.setDoOutput(true);
    con.setUseCaches(false);
    con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
    //con.setRequestProperty("Content-Length", ""+ body.length());
    //outStream = new DataOutputStream(con.getOutputStream());
 // Send request
    //outStream.writeBytes(body);
    //outStream.flush();
    //outStream.close();
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




**Step 3. Some other steps like.**
 a. You may need to change your port
 b. You may need to create ssl certificate
 c. change php.ini in apache and php in  wamp to enable curl and ssl.
 d. You can send arguments from java to php
