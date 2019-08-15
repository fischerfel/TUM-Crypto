import java.io.*;
import java.net.*;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

import javax.net.ssl.*;

public class SslServer
{
    private static final int PORT = 5555;

    public static void main(String[] args)
    {
        SecureRandom sr = new SecureRandom();
        sr.nextInt();

        try {
            //client.public is the keystore file that holds the client's public key (created with keytool)
            KeyStore clientKeyStore = KeyStore.getInstance("JKS");
            clientKeyStore.load(new FileInputStream("client.public"), "clientpublicpw".toCharArray());

            //server.private is the key pair for the server (created with keytool)
            KeyStore serverKeyStore = KeyStore.getInstance("JKS");
            clientKeyStore.load(new FileInputStream("server.private"), "serverprivatepw".toCharArray());

            TrustManagerFactory tmf = TrustManagerFactory.getInstance("SunX509");
            tmf.init(clientKeyStore);

            //This next line is where the exception occurs
            KeyManagerFactory kmf = KeyManagerFactory.getInstance("TLS");
            kmf.init(serverKeyStore, "serverprivatepw".toCharArray());

            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(kmf.getKeyManagers(), tmf.getTrustManagers(), sr);

            SSLServerSocketFactory sf = sslContext.getServerSocketFactory();
            SSLServerSocket ss = (SSLServerSocket)sf.createServerSocket(SslServer.PORT);
            ss.setNeedClientAuth(true);

            BufferedReader in = new BufferedReader(new InputStreamReader(ss.accept().getInputStream()));

            String line = null;
            while((line = in.readLine()) != null)
            {
                System.out.println(line);
            }
            in.close();
            ss.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (CertificateException e) {
            e.printStackTrace();
        } catch (KeyStoreException e) {
            e.printStackTrace();
        } catch (UnrecoverableKeyException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }
    }

}
