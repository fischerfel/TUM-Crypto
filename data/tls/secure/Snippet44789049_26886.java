import java.security.*;
import javax.net.ssl.*;
import java.net.URL;


URL url = new URL("https://www.google.com");

SSLContext ssl = SSLContext.getInstance("TLSv1.2"); 
ssl.init(null, null, new SecureRandom());

HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
connection.setSSLSocketFactory(ssl.getSocketFactory());
