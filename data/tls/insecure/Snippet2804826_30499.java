package util;

/**
 * @author spaduri
 *
 */
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

import javax.net.SocketFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;

public class CustomSSLSocketFactory extends SSLSocketFactory {

    private SSLSocketFactory factory;

    public CustomSSLSocketFactory() {
        try {
            SSLContext sslcontext = null;
              // Call getKeyManagers to get suitable key managers
            KeyManager[] kms=getKeyManagers();
            if (sslcontext == null) {
                sslcontext = SSLContext.getInstance("SSL");
                sslcontext.init(kms,
                new TrustManager[] { new CustomTrustManager() },
                new java.security.SecureRandom());
            }
            factory = (SSLSocketFactory) sslcontext.getSocketFactory();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    public static SocketFactory getDefault() {
        return new CustomSSLSocketFactory();
    }

    public Socket createSocket(Socket socket, String s, int i, boolean flag) throws IOException {
        return factory.createSocket(socket, s, i, flag);
    }

    public Socket createSocket(InetAddress inaddr, int i, InetAddress inaddr1, int j) throws IOException {
        return factory.createSocket(inaddr, i, inaddr1, j);
    }

    public Socket createSocket(InetAddress inaddr, int i) throws IOException {
        return factory.createSocket(inaddr, i);
    }

    public Socket createSocket(String s, int i, InetAddress inaddr, int j) throws IOException {
        return factory.createSocket(s, i, inaddr, j);
    }

    public Socket createSocket(String s, int i) throws IOException {
        return factory.createSocket(s, i);
    }

    public String[] getDefaultCipherSuites() {
        return factory.getSupportedCipherSuites();
    }

    public String[] getSupportedCipherSuites() {
        return factory.getSupportedCipherSuites();
    }

 protected KeyManager[] getKeyManagers()
        throws IOException, GeneralSecurityException
      {
        // First, get the default KeyManagerFactory.
        String alg=KeyManagerFactory.getDefaultAlgorithm();
        KeyManagerFactory kmFact=KeyManagerFactory.getInstance(alg);

        // Next, set up the KeyStore to use. We need to load the file into
        // a KeyStore instance.

        ClassLoader cl = CustomSSLSocketFactory.class.getClassLoader();
        // read the file someTrustStore from the jar file from a classpath
        InputStream in = cl.getResourceAsStream("ssl/someTruststore.jks");
        //FileInputStream fis=new FileInputStream(adentTruststore.jks);
        KeyStore ks=KeyStore.getInstance("jks");
        ks.load(in, null);
        in.close();

        // Now we initialise the KeyManagerFactory with this KeyStore
        kmFact.init(ks, null);

        // And now get the KeyManagers
        KeyManager[] kms=kmFact.getKeyManagers();
        return kms;
      }
}
