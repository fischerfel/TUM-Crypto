import java.net.URL;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.io.*;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class JavaHttpsExample
{
  public static void main(String[] args)
  throws Exception
  {
    String httpsURL = "https://a.b.c.d/zz?wsdl";
    URL myurl = new URL(httpsURL);
    SSLContext ssl = SSLContext.getInstance("TLSv1");
    ssl.init(null, new TrustManager[]{new SimpleX509TrustManager()}, null);
    SSLSocketFactory factory = ssl.getSocketFactory();


    HttpsURLConnection con = (HttpsURLConnection)myurl.openConnection();
    con.setSSLSocketFactory(factory);
    InputStream ins = con.getInputStream();
    InputStreamReader isr = new InputStreamReader(ins);
    BufferedReader in = new BufferedReader(isr);

    String inputLine;

    while ((inputLine = in.readLine()) != null)
    {
      System.out.println(inputLine);
    }

    in.close();
  }
}

class SimpleX509TrustManager implements X509TrustManager {
    public void checkClientTrusted(
            X509Certificate[] cert, String s)
            throws CertificateException {
    }

    public void checkServerTrusted(
            X509Certificate[] cert, String s)
            throws CertificateException {
      }

    @Override
    public X509Certificate[] getAcceptedIssuers() {
        // TODO Auto-generated method stub
        return null;
    }

}
