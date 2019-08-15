import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;

public class SSLClient {

    public static final int PORT_NO = 9020;
    static void doProtocol( Socket cSock ) throws IOException
    {
        OutputStream     out = cSock.getOutputStream();
        InputStream      in = cSock.getInputStream();

        out.write("World".getBytes());
        out.write('!');

        int ch = 0;
        while ((ch = in.read()) != '!')
        {
            System.out.print((char)ch);
        }

        System.out.println((char)ch);
    }
    static SSLContext createSSLContext() throws Exception
    {
        // set up a key manager for our local credentials
        KeyManagerFactory mgrFact = KeyManagerFactory.getInstance("SunX509");
//      KeyStore clientStore = KeyStore.getInstance("PKCS12");

//      clientStore.load(new FileInputStream("client.p12"), "Password");

//      mgrFact.init(clientStore, "password");

        // create a context and set up a socket factory
        SSLContext sslContext = SSLContext.getInstance("TLS");

        sslContext.init(mgrFact.getKeyManagers(), null, null);

        return sslContext;
}

    public static void main( String[] args ) throws Exception
    {
        SSLContext       sslContext = createSSLContext();
        SSLSocketFactory fact = sslContext.getSocketFactory();
//        SSLSocketFactory fact = (SSLSocketFactory)SSLSocketFactory.getDefault();
        SSLSocket        cSock = (SSLSocket)fact.createSocket("localhost", PORT_NO);

        doProtocol(cSock);
    }
}
