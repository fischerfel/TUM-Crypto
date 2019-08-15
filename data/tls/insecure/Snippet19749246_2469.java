import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.TrustManagerFactory;

public class Main
{
    public static void main(String[] args)
    {
        SSLContext context;
        KeyManagerFactory kmf;
        KeyStore ks;
        char[] storepass = "password".toCharArray();
        char[] keypass = "password".toCharArray();
        String storename = "plainserver.jks";
        try
        {
            //KEY MANAGER
            context = SSLContext.getInstance("TLS");
            kmf = KeyManagerFactory.getInstance("SunX509");
            FileInputStream fin = new FileInputStream(storename);
            ks = KeyStore.getInstance("JKS");
            ks.load(fin, storepass);
            kmf.init(ks, keypass);

            //TRUST MANAGER
            KeyStore clientPub = KeyStore.getInstance("JKS");
            clientPub.load(new FileInputStream("clientpub.jks"),"password".toCharArray());
            TrustManagerFactory trustManager = TrustManagerFactory.getInstance("SunX509");
            trustManager.init(clientPub);

            //INIT SERVER STUFF
            context.init(kmf.getKeyManagers(), trustManager.getTrustManagers(), null);
            SSLServerSocketFactory ssf = context.getServerSocketFactory();
            //ServerSocket ss = ssf.createServerSocket(443);
            SSLServerSocket ss = (SSLServerSocket) ssf.createServerSocket(443);

            while (true)
            {
                System.out.println("ACCEPT");
                Socket s = ss.accept();
                System.out.println("PROCESS! (1)");
                BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));
                System.out.println("PROCESS! (2)");
                String x = in.readLine();       //!!!!!PROGRAM WILL CRASH HERE!!!!
                System.out.println("PROCESS! (3)");
                System.out.println(x);
                s.close();
            }
        }
        catch (KeyStoreException | IOException | NoSuchAlgorithmException | KeyManagementException | 
                UnrecoverableKeyException | CertificateException e)
        {
            e.printStackTrace();
        }
    }
}
