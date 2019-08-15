import org.junit.Test;

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

    @Test
    public void testJKSKeyStore() throws Exception {
        final String KEYSTOREPATH = "clientkeystore.jks";
        final char[] KEYSTOREPASS = "keystorepass".toCharArray();
        final char[] KEYPASS = "keypass".toCharArray();

        try (InputStream storeStream = this.getClass().getResourceAsStream(KEYSTOREPATH)) {
            setSSLFactories(storeStream, "JKS", KEYSTOREPASS, KEYPASS);
        }
        testPlainJavaHTTPS();
    }
    @Test
    public void testP12KeyStore() throws Exception {
        final String KEYSTOREPATH = "clientkeystore.p12";
        final char[] KEYSTOREPASS = "keystorepass".toCharArray();
        final char[] KEYPASS = "keypass".toCharArray();

        try (InputStream storeStream = this.getClass().getResourceAsStream(KEYSTOREPATH)) {
            setSSLFactories(storeStream, "PKCS12", KEYSTOREPASS, KEYPASS);
        }
        testPlainJavaHTTPS();
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

    public void testPlainJavaHTTPS() throws Exception {
        String httpsURL = "https://slsh.iki.fi/client-certificate/protected/";
        URL myUrl = new URL(httpsURL);
        HttpsURLConnection conn = (HttpsURLConnection)myUrl.openConnection();
        try (InputStream is = conn.getInputStream()) {
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);

            String inputLine;

            while ((inputLine = br.readLine()) != null) {
                System.out.println(inputLine);
            }
        }
    }
}
