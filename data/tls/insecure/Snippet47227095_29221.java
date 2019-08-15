import it.sauronsoftware.ftp4j.FTPClient;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import java.io.FileInputStream;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.util.Arrays;

public class FTP {

    public static void main(String args[]) throws Exception {
        System.setProperty("http.protocols", "TLSv1,TLSv1.1,TLSv1.2"); 
        //tried to avoid closing connection during the handshake

        //load and set certificate
        KeyStore keyStore = KeyStore.getInstance("JKS");
        keyStore.load(new FileInputStream("mykeystore.jks"), "root12".toCharArray());

        FTPClient client = new FTPClient();

        SSLContext sslContext = null;
        try {
            javax.net.ssl.TrustManagerFactory tmf = javax.net.ssl.TrustManagerFactory
                    .getInstance(javax.net.ssl.KeyManagerFactory
                            .getDefaultAlgorithm());
            tmf.init(keyStore);
            sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, tmf.getTrustManagers(), new SecureRandom());
        } catch (Exception e) {
            e.printStackTrace();
        }

        SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

        client.setSSLSocketFactory(sslSocketFactory);
        client.setSecurity(FTPClient.SECURITY_FTPES);

        client.setCharset("UTF-8");
        client.setPassive(true);
        String[] arg = client.connect("localhost", 21);
        System.out.println(Arrays.toString(arg));

        client.login("admin", "pass"); //OK
        client.noop(); // aka Ping is OK
        String s = client.currentDirectory(); //OK
        client.changeDirectory("/"); //OK
        String[] files = client.listNames(); //Exception here
        System.out.println(Arrays.toString(files));
        client.disconnect(true);
    }
}
