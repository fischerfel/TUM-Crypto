import java.io.*;
import java.net.*;
import java.security.*;
import javax.net.ssl.*;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

public class SSLReverseEchoer {
  public static void main(String[] args) {
     String ksName = "sslkeystore.jks";
     String keystorePass = "sslkeystorepassword";
     char ksPass[] = keystorePass.toCharArray();

     int sslPort = 9099; 

     Security.addProvider(new BouncyCastleProvider());

     File file = new File(ksName);
     String absPath = file.getAbsolutePath(); 

     System.setProperty("javax.net.ssl.keyStore", absPath); 
     System.setProperty("javax.net.ssl.keyStorePassword", "sslkeystorepassword");

     try {

         //Get keystore w/ password
        KeyStore ks = KeyStore.getInstance("JKS");
        ks.load(new FileInputStream(ksName), ksPass);

        //Trust Manager
        TrustManagerFactory tmf = TrustManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
        tmf.init(ks);

        KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
        kmf.init(ks, ksPass);

        SSLContext sc = SSLContext.getInstance("TLS");
        sc.init(kmf.getKeyManagers(), tmf.getTrustManagers(), new SecureRandom());

        SSLServerSocketFactory ssf = sc.getServerSocketFactory();
        SSLServerSocket s = (SSLServerSocket) ssf.createServerSocket(sslPort);
        printServerSocketInfo(s);

        //WAIT FOR CONNECTION TODO ADD THREAD for NIO
        SSLSocket c = (SSLSocket) s.accept();
        printSocketInfo(c);

        BufferedWriter w = new BufferedWriter(new OutputStreamWriter(
           c.getOutputStream()));
        BufferedReader r = new BufferedReader(new InputStreamReader(
           c.getInputStream()));

        String m = "Welcome to SSL Reverse Echo Server."+
           " Please type in some words.";
        w.write(m,0,m.length());
        w.newLine();
        w.flush();
        while ((m=r.readLine())!= null) {
           if (m.equals(".")) break;
           char[] a = m.toCharArray();
           int n = a.length;
           for (int i=0; i<n/2; i++) {
              char t = a[i];
              a[i] = a[n-1-i];
              a[n-i-1] = t;
           }
           w.write(a,0,n);
           w.newLine();
           w.flush();
        }
        w.close();
        r.close();
        c.close();
        s.close();
     } catch (Exception e) {
        e.printStackTrace();
     }
  }
  private static void printSocketInfo(SSLSocket s) {
     System.out.println("Socket class: "+s.getClass());
     System.out.println("   Remote address = "
        +s.getInetAddress().toString());
     System.out.println("   Remote port = "+s.getPort());
     System.out.println("   Local socket address = "
        +s.getLocalSocketAddress().toString());
     System.out.println("   Local address = "
        +s.getLocalAddress().toString());
     System.out.println("   Local port = "+s.getLocalPort());
     System.out.println("   Need client authentication = "
        +s.getNeedClientAuth());
     SSLSession ss = s.getSession();
     System.out.println("   Cipher suite = "+ss.getCipherSuite());
     System.out.println("   Protocol = "+ss.getProtocol());
  }
  private static void printServerSocketInfo(SSLServerSocket s) {
     System.out.println("Server socket class: "+s.getClass());
     System.out.println("   Socket address = "
        +s.getInetAddress().toString());
     System.out.println("   Socket port = "
        +s.getLocalPort());
     System.out.println("   Need client authentication = "
        +s.getNeedClientAuth());
     System.out.println("   Want client authentication = "
        +s.getWantClientAuth());
     System.out.println("   Use client mode = "
        +s.getUseClientMode());
  } 
}
