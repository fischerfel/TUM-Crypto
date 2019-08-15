import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.cert.X509Certificate;
import javax.imageio.ImageIO;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;


/**
 * @author Syed CBE
 *
 */
public class Main {


    public static void main(String[] args) {

                int height = 0,width = 0;
                String imagePath="https://www.sampledomain.com/sampleimage.jpg";
                System.out.println("URL=="+imagePath);
                InputStream connection;
                try {
                    URL url = new URL(imagePath);  
                    if(imagePath.indexOf("https://")!=-1){
                        final SSLContext sc = SSLContext.getInstance("SSL");
                        sc.init(null, getTrustingManager(), new java.security.SecureRandom());                                 
                        HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
                        connection = url.openStream();
                     }
                    else{
                        connection = url.openStream();
                    }
                    BufferedImage bufferedimage = ImageIO.read(connection);
                    width          = bufferedimage.getWidth();
                    height         = bufferedimage.getHeight();
                    System.out.println("width="+width);
                    System.out.println("height="+height);
                } catch (MalformedURLException e) {

                    System.out.println("URL is not correct : " + imagePath);
                } catch (IOException e) {

                    System.out.println("IOException Occurred : "+e);
                }
                catch (Exception e) {

                    System.out.println("Exception Occurred  : "+e);
                }


    }

     private static TrustManager[] getTrustingManager() {
            TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {

                   @Override
                   public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                         return null;
                   }

                   @Override
                   public void checkClientTrusted(X509Certificate[] certs, String authType) {
                   }

                   @Override
                   public void checkServerTrusted(X509Certificate[] certs, String authType) {
                   }
            } };
            return trustAllCerts;
     }

}
