import java.io.BufferedInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.security.KeyStore;
import java.util.Arrays;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;

public class Minimal {
    public static void main(String[] args) throws Exception {
        SSLContext context = SSLContext.getInstance("SSL");
        KeyManagerFactory keyFac = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
        KeyStore keyStore = KeyStore.getInstance("WINDOWS-MY");
        keyStore.load(null, null);
        keyFac.init(keyStore, null);
        TrustManagerFactory trustFac = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        KeyStore trustStore = KeyStore.getInstance("WINDOWS-ROOT");
        trustStore.load(null, null);
        trustFac.init(trustStore);
        context.init(keyFac.getKeyManagers(), trustFac.getTrustManagers(), null);

        HttpsURLConnection conn = (HttpsURLConnection)new URL("https://<redacted>").openConnection();
        conn.setRequestMethod("GET");
        conn.setDoInput(true);
        conn.setSSLSocketFactory(context.getSocketFactory());

        int responseCode = conn.getResponseCode();
        System.out.println("RESPONSE: " + responseCode);
        InputStream response = null;
        if(responseCode != 200) {
            response = conn.getErrorStream();
        } else {
            response = conn.getInputStream();
        }
        Reader r = new InputStreamReader(new BufferedInputStream(response));
        char[] buffer = new char[1024];
        int read = 0;
        while((read = r.read(buffer)) != -1) {
            System.out.print(Arrays.copyOf(buffer, read));
        }
        System.out.println("DONE");
    }
}
