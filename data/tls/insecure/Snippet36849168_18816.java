package authserv;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.net.SocketException;
import java.net.URL;
import java.security.KeyStore;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLHandshakeException;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;

public class AuthClient{
    static String NO_KEYSTORE = "";
    static String UNAUTH_KEYSTORE = "keys/unauthclient.jks"; // Doesn't exist in server trust store, should fail authentication.
    static String AUTH_KEYSTORE = "keys/authclient.jks"; // Exists in server trust store, should pass authentication.
    static String TRUSTSTORE = "keys/clienttrust.jks";
    static String CLIENT_PWD = "aaaaaa";

    public static void main(String[] args) throws Exception {

        AuthClient cl = new AuthClient();
        System.out.println("No keystore:");
        cl.testIt(NO_KEYSTORE);
        System.out.println("Unauth keystore:");
        cl.testIt(UNAUTH_KEYSTORE);
        System.out.println("Auth keystore:");
        cl.testIt(AUTH_KEYSTORE);
    }

    public void testIt(String jksFile){
        try {
            String https_url = "https://localhost:8888/auth/";
            URL url;
            url = new URL(https_url);
            HttpsURLConnection conn = (HttpsURLConnection)url.openConnection();
            conn.setSSLSocketFactory(getSSLFactory(jksFile));

            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.setUseCaches(false);

            // Print response
            BufferedReader bir = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line = null;
            while((line = bir.readLine()) != null) {
                  System.out.println(line);
                }
            bir.close();
            conn.disconnect();
        } catch (SSLHandshakeException|SocketException e) {
            System.out.println(e.getMessage());
            System.out.println("");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static SSLSocketFactory getSSLFactory(String jksFile) throws Exception {
        // Create key store
        KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
        KeyManager[] kmfs = null;
        if( jksFile.length() > 0 ) {
            keyStore.load(new FileInputStream(jksFile), CLIENT_PWD.toCharArray());
            KeyManagerFactory kmf = KeyManagerFactory.getInstance(
                        KeyManagerFactory.getDefaultAlgorithm());
            kmf.init(keyStore, CLIENT_PWD.toCharArray());
            kmfs = kmf.getKeyManagers();
        }

        // create trust store (validates the self-signed server!)
        KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
        trustStore.load(new FileInputStream(TRUSTSTORE), CLIENT_PWD.toCharArray());
        TrustManagerFactory trustFactory = TrustManagerFactory.getInstance(
                        TrustManagerFactory.getDefaultAlgorithm());
        trustFactory.init(trustStore);

        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(kmfs, trustFactory.getTrustManagers(), null);
        return sslContext.getSocketFactory();
    }
}
