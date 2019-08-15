import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLServerSocketFactory;

public class HttpsNetServer {


    public static void main(String args[]) throws Exception {
        SSLContext context;
        KeyManagerFactory kmf;
        KeyStore ks;
        char[] storepass = "changeit".toCharArray();
        char[] keypass = "changeit".toCharArray();
        String storename = "/home/krishnan/.keystore";


        context = SSLContext.getInstance("TLS");
        kmf = KeyManagerFactory.getInstance("SunX509");
        FileInputStream fin = new FileInputStream(storename);
        ks = KeyStore.getInstance("JKS");
        ks.load(fin, storepass);

        kmf.init(ks, keypass);
        context.init(kmf.getKeyManagers(), null, null);
        SSLServerSocketFactory ssf = context.getServerSocketFactory();

        ServerSocket ss = ssf.createServerSocket(8000);

          Socket s = ss.accept();
          PrintStream out = new PrintStream(s.getOutputStream());
          out.println("Hi");
          out.close();
          s.close();

    }

}
