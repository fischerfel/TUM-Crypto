 import java.io.IOException;
 import java.net.MalformedURLException;
 import java.net.URL;
 import java.net.URLConnection;
 import java.util.Properties;
 import javax.net.ssl.HostnameVerifier;
 import javax.net.ssl.HttpsURLConnection;
 import javax.net.ssl.SSLSession;


 public class ConnectToDemoSite {

/**
 * @param args
 */
public static void main(String[] args) {
    // TODO Auto-generated method stub

    String urlStr = "https://www.myDemoSite.com/demo1/";
    URL url;
    Properties reply = new Properties();
    try {
        url = new URL(urlStr);
        URLConnection conn = url.openConnection();
        if(conn instanceof HttpsURLConnection)
        {
        HttpsURLConnection conn1 = (HttpsURLConnection)url.openConnection();
            conn1.setHostnameVerifier(new HostnameVerifier()  
            {        
                public boolean verify(String hostname, SSLSession session)  
                {  
                    return true;  
                }  
            });  

        reply.load(conn1.getInputStream());
        }
        else 
        {
             conn = url.openConnection();
             reply.load(conn.getInputStream());
        }
    } catch (MalformedURLException e) {
       e.printStackTrace();
        e.printStackTrace();
    } catch (IOException e) {
        e.printStackTrace();
    }
    catch (Exception e) {
        e.printStackTrace();
    }
    System.out.println("reply is"+reply);



 }

}
