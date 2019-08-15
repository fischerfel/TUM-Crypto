import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.UnrecoverableKeyException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.util.Enumeration;

import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.java_websocket.WebSocketImpl;

public class TestClient {

    private static final Log log = LogFactory.getLog(TestClient.class);

    public static void main(String[] args) throws URISyntaxException {
        WebSocketImpl.DEBUG = true;

        WSRAClient wsRaClient = new WSRAClient(new URI("wss://localhost:8443/TestWebSocket-0.0.1-SNAPSHOT/test"));

        String keystoreFile = "keystore.p12";
        String keystorePassword = "keystore";

        String truststoreFile = "truststore.jks";
        String truststorePassword = "truststore";


        try {
            SSLContext ssl = SSLContext.getInstance("TLSv1.2");

            log.info("Configuring SSL keystore");
            KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm()); 
            KeyStore store = KeyStore.getInstance(KeyStore.getDefaultType());
            log.debug("Loading keystore");
            store.load(new FileInputStream(keystoreFile), keystorePassword.toCharArray());
            log.debug("Number of keystore certificates: " + store.size());
            Enumeration<String> enumeration = store.aliases();
            while(enumeration.hasMoreElements()) {
                String alias = enumeration.nextElement();
                log.debug("alias name: " + alias);
                Certificate certificate = store.getCertificate(alias);
                log.debug(certificate.toString());
            }
            kmf.init(store, keystorePassword.toCharArray());
            KeyManager[] keyManagers = new KeyManager[1];
            keyManagers = kmf.getKeyManagers();

            log.info("Configuring SSL truststore");
            TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            KeyStore truststore = KeyStore.getInstance(KeyStore.getDefaultType());
            log.debug("Loading truststore");
            truststore.load(new FileInputStream(truststoreFile), truststorePassword.toCharArray());
            log.debug("Number of truststore certificates: " + truststore.size());
            enumeration = truststore.aliases();
            while(enumeration.hasMoreElements()) {
                String alias = (String)enumeration.nextElement();
                log.debug("alias name: " + alias);
                Certificate certificate = truststore.getCertificate(alias);
                log.debug(certificate.toString());
            }
            tmf.init(truststore);
            TrustManager[] trustManagers = tmf.getTrustManagers();

            ssl.init(keyManagers, trustManagers, new SecureRandom());

            SSLSocketFactory factory = ssl.getSocketFactory();// (SSLSocketFactory) SSLSocketFactory.getDefault();

            wsRaClient.setSocket(factory.createSocket());

            wsRaClient.connectBlocking();

            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            while ( true ) {
                String line = reader.readLine();
                if(line.equals("close")) {
                    wsRaClient.close();
                } else {
                    wsRaClient.send(line);
                }
            }

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            log.error(e);
            System.exit(0);
        } catch (KeyStoreException e) {
            e.printStackTrace();
            log.error(e);
            System.exit(0);
        } catch (CertificateException e) {
            e.printStackTrace();
            log.error(e);
            System.exit(0);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            log.error(e);
            System.exit(0);
        } catch (IOException e) {
            e.printStackTrace();
            log.error(e);
            System.exit(0);
        } catch (UnrecoverableKeyException e) {
            e.printStackTrace();
            log.error(e);
            System.exit(0);
        } catch (KeyManagementException e) {
            e.printStackTrace();
            log.error(e);
            System.exit(0);
        } catch (InterruptedException e) {
            e.printStackTrace();
            log.error(e);
            System.exit(0);
        }

    }

}
