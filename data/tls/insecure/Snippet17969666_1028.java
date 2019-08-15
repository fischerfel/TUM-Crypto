import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class TestHTTPS{

public static void main(String[] args) throws InterruptedException
{
    new TestHTTPS().activate();
}

private void activate() throws InterruptedException{

    TrustManager[] insecureTrustManager = new TrustManager[] { 
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


    try {
        SSLContext sc = SSLContext.getInstance("SSL"); 
        sc.init(null, insecureTrustManager, new java.security.SecureRandom());
        HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
    } catch (KeyManagementException e1) {
        // TODO Auto-generated catch block
        e1.printStackTrace();
    } catch (NoSuchAlgorithmException e) {
        e.printStackTrace();
    } 

    HostnameVerifier allHostsValid = new HostnameVerifier() {
        public boolean verify(String hostname, SSLSession session) {
            return true;
        }
    };

    HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);

    String https_url = "https://192.168.xx.xx:8443";
    URL url;
    try {

        url = new URL(https_url);

        while(true) {
            HttpsURLConnection con = (HttpsURLConnection)url.openConnection();
            con.setConnectTimeout(1000);
            print_content(con);
            Thread.sleep(100);
        }

    } catch (MalformedURLException e) {
        e.printStackTrace();
    } catch (IOException e) {
        e.printStackTrace();
    }

}

private void print_content(HttpsURLConnection con){
    if(con!=null){

        try {

            System.out.println("****** Content of the URL ********");           
            BufferedReader br = 
                    new BufferedReader(
                            new InputStreamReader(con.getInputStream()));

            String input;

            while ((input = br.readLine()) != null){
                System.out.println(input);
            }
            br.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
