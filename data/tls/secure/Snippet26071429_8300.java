package testing;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.security.KeyStore;

import javax.net.ServerSocketFactory;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.TrustManager;

public class SSLServerTest {
    public static void main(String[] args) {
        try {
            int port = 3000;
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
            ServerSocketFactory ssocketFactory = sc.getServerSocketFactory();
            SSLServerSocket ssocket = (SSLServerSocket) ssocketFactory
                    .createServerSocket(port);
            ssocket.setEnabledProtocols(new String[] { "SSLv3" });
            Socket socket = ssocket.accept();
            BufferedReader in = new BufferedReader(new InputStreamReader(
                    socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream());
            out.println("Hello, Securly!");
            out.close();
            in.close();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
