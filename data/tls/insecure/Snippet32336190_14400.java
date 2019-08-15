import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.Random;

import javax.net.ssl.*;

public class Server {

    private String ksname;
    private char kspass[], ctpass[];
    private int port;
    private InetAddress ip;

    public Server(){
        ksname = null;
        kspass = new char[200];
        ctpass = new char[200];
        kspass = null;ctpass = null;
        port = 10000 + (new Random()).nextInt(10000);
        try {
            ip = InetAddress.getByName("127.0.0.1");
        } catch (UnknownHostException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public Server(String cname, String cpass, String pass, int p, String host){
        ksname = cname;
        kspass = new char[200];
        kspass = cpass.toCharArray();
        ctpass = new char[200];
        ctpass = pass.toCharArray();
        port = p;
        try {
            ip = InetAddress.getByName(host);
        } catch (UnknownHostException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public SSLServerSocket getServer(){
        SSLServerSocket s = null;
        try {
            KeyStore ks = KeyStore.getInstance("JKS");
            ks.load(new FileInputStream(ksname), kspass);
            KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
            kmf.init(ks, ctpass);
            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(kmf.getKeyManagers(), null, null);
            SSLServerSocketFactory ssf = sc.getServerSocketFactory();
            s = (SSLServerSocket) ssf.createServerSocket(port,0,ip);
            System.out.println("Server created on port " + port +"\n");
        } catch (KeyStoreException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (CertificateException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (KeyManagementException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (UnrecoverableKeyException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return s;
    }
}
