package testing;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.KeyStore;

import javax.net.SocketFactory;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.TrustManager;

public class SSLClientTest {
    public static void main(String[] args) {

        int port = 3000;
        String host = "localhost";

        try {
            SSLContext sc = SSLContext.getInstance("TLSv1.2");
            KeyStore ks = KeyStore.getInstance("JKS");
            InputStream ksIs = new FileInputStream("key.txt");
            try {
                ks.load(ksIs, "Bennett556".toCharArray());
            } finally {
                if (ksIs != null) {
                    ksIs.close();
                }
            }
            KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
            kmf.init(ks, "Bennett556".toCharArray());
            sc.init(kmf.getKeyManagers(), new TrustManager[] {}, null);
            SocketFactory factory = sc.getSocketFactory();
            SSLSocket socket = (SSLSocket) factory.createSocket(host, port);
            socket.startHandshake();
            BufferedReader in = new BufferedReader(new InputStreamReader(
                    socket.getInputStream()));
            String str = "";
            while ((str = in.readLine()) != null)
                System.out.println(str);
            in.close();
            socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
