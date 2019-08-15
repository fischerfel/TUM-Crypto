 import java.net.*;
 import java.io.*;
 import javax.net.ssl.*;
 import javax.security.cert.X509Certificate;
 import java.security.KeyStore;


public class SSLServerWithAuth{

public static final int PORT_NUMBER = 6060;

    public static void main(String[] argv) {

        char[] passphrase = "changeit".toCharArray();

        //Need to set up TrustManager/TrustStore, and MessageDigest? Maybe.
        try {
            KeyStore ks = KeyStore.getInstance("JKS");
            ks.load(new FileInputStream("mySrvKeystore.jks"), passphrase);

            KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
            kmf.init(ks, passphrase);
            SSLContext ctx = SSLContext.getInstance("SSL");
            ctx.init(kmf.getKeyManagers(), null, null);
            SSLServerSocketFactory ssf = ctx.getServerSocketFactory();
            SSLServerSocket ss = (SSLServerSocket) ssf.createServerSocket(6060);
            SSLSocket s = (SSLSocket) ss.accept();

            ss.setNeedClientAuth(true);


        //  if(/** Name**/.equals("-a")) {

                byte [] mybytearray  = new byte [16*1024];
                InputStream is = s.getInputStream();
                FileOutputStream fos = new FileOutputStream("test.txt");
                BufferedOutputStream bos = new BufferedOutputStream(fos);

                int bytesRead = is.read(mybytearray,0,mybytearray.length);

                int current = bytesRead;

                do {
                    bytesRead = is.read(mybytearray, current, (mybytearray.length-current));
                    if(bytesRead >= 0) current += bytesRead;
                } while(bytesRead > -1);

                bos.write(mybytearray, 0 , current);
                bos.flush();
                s.close();
        //  }
        }
        catch (Exception e) {
            System.err.println(e.toString());
            }



    }
