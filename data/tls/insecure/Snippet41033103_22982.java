import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.KeyStore;
import java.security.Principal;
import java.util.Date;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLPeerUnverifiedException;
import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.TrustManagerFactory;
import javax.security.auth.x500.X500Principal;

//import org.bouncycastle.cert.X509v3CertificateBuilder;
//import org.bouncycastle.crypto.tls.Certificate;
//import org.bouncycastle.jcajce.provider.asymmetric.x509.KeyFactory;


public class SSLSocketServer {

    public static int SERVER_PORT = 9020;


    static boolean isEndEntity( SSLSession session ) throws SSLPeerUnverifiedException
    {
            Principal id = session.getPeerPrincipal();
            if (id instanceof X500Principal)
            {
                X500Principal x500 = (X500Principal)id;
                String Expireddate = x500.getName("Expireddate");
                String Name = x500.getName("Name");
                return (Name.equals("James") && Date.parse(Expireddate) > System.currentTimeMillis());
            }

            return false;
     }


    /**
     * Carry out the '!' protocol - server side.
     */
    static void doProtocol(
        Socket sSock)
        throws IOException
    {
        System.out.println("session started.");

        InputStream in = sSock.getInputStream();
        OutputStream out = sSock.getOutputStream();
        // Send Key

        out.write("Hello ".getBytes());

        int ch = 0;
        while ((ch = in.read()) != '!')
        {
            out.write(ch);
        }

        out.write('!');

        sSock.close();
    }
    /**
     * Create an SSL context with identity and trust stores in place
     */
    SSLContext createSSLContext() 
        throws Exception
    {
        // set up a key manager for our local credentials
        KeyManagerFactory mgrFact = KeyManagerFactory.getInstance("SunX509");
        KeyStore serverStore = KeyStore.getInstance("JKS");

        serverStore.load(new FileInputStream("server.jks"), "password".toCharArray());

        mgrFact.init(serverStore, "password".toCharArray());

        // set up a trust manager so we can recognize the server
        TrustManagerFactory trustFact = TrustManagerFactory.getInstance("SunX509");
        KeyStore            trustStore = KeyStore.getInstance("JKS");

        trustStore.load(new FileInputStream("trustStore.jks"), "trustpassword".toCharArray());

        trustFact.init(trustStore);

        // create a context and set up a socket factory
        SSLContext sslContext = SSLContext.getInstance("TLS");

        sslContext.init(mgrFact.getKeyManagers(), trustFact.getTrustManagers(), null);

        return sslContext;
    }


    public static void main( String[] args ) throws Exception
    {
        SSLServerSocketFactory fact = (SSLServerSocketFactory)SSLServerSocketFactory.getDefault();
        SSLServerSocket        sSock = (SSLServerSocket)fact.createServerSocket(SERVER_PORT);

        SSLSocket sslSock = (SSLSocket)sSock.accept();
        sSock.setNeedClientAuth(false); // current ignore

        doProtocol(sslSock);
    }
}
