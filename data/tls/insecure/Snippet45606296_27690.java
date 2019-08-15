import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.security.KeyStore;

public class PlainJavaHTTPS2Test {

    public void setUp() throws Exception {
        final String KEYSTOREPATH = "clientkeystore.p12"; // or .jks

        // store password can be null if there is no password
        final char[] KEYSTOREPASS = "keystorepass".toCharArray();

        // key password can be null if there is no password
        final char[] KEYPASS = "keypass".toCharArray();

        try (InputStream storeStream = this.getClass().getResourceAsStream(KEYSTOREPATH)) {
            setSSLFactories(storeStream, "PKCS12", KEYSTOREPASS, KEYPASS);
        }
    }
    private static void setSSLFactories(InputStream keyStream, String keystoreType, char[] keyStorePassword, char[] keyPassword) throws Exception
    {
        KeyStore keyStore = KeyStore.getInstance(keystoreType);

        keyStore.load(keyStream, keyStorePassword);

        KeyManagerFactory keyFactory =
                KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());

        keyFactory.init(keyStore, keyPassword);

        KeyManager[] keyManagers = keyFactory.getKeyManagers();

        SSLContext sslContext = SSLContext.getInstance("SSL");
        sslContext.init(keyManagers, null, null);
        SSLContext.setDefault(sslContext);
    }
}
