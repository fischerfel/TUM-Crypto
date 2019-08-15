import javax.net.ssl.*;
import javax.swing.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.security.KeyStore;
import java.security.SecureRandom;


public class main {
    public static void main(String[] args) {
        try {

            //Load keystore
            KeyStore keystore = KeyStore.getInstance("JKS");
            keystore.load(new FileInputStream(new File("D:\\keystore.asdf")), "123456".toCharArray());

            //Prepare SSLServerSocket
            SecureRandom r = SecureRandom.getInstanceStrong();
            KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
            TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            kmf.init(keystore,"123456".toCharArray());
            tmf.init(keystore);

            //initialize SSLContext
            SSLContext sslContext = SSLContext.getInstance("TLSv1.2");
            sslContext.init(kmf.getKeyManagers(),tmf.getTrustManagers(),r);
            SSLSocketFactory factory = sslContext.getSocketFactory();
            SSLSocket socket = (SSLSocket) factory.createSocket();

            socket.setEnabledProtocols(new String[]{"TLSv1.2"});
            socket.setEnabledCipherSuites(new String[]{"TLS_ECDHE_ECDSA_WITH_AES_256_GCM_SHA384"});
            socket.setEnableSessionCreation(true);
            socket.connect(new InetSocketAddress("localhost",6060));
            socket.startHandshake();
            PrintWriter out = new PrintWriter(socket.getOutputStream(),true);

            while (true) {
                String msg = JOptionPane.showInputDialog(null,"Message");
                if (!msg.equalsIgnoreCase("")) {
                    out.println(msg);
                } else {
                    socket.close();
                    break;
                }
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}
