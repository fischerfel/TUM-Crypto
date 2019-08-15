 public static void setTruststore(String truststorePath, String truststorePwd)
    {
        if( truststorePath != null && !truststorePath.trim().equalsIgnoreCase("null") && truststorePath.trim().length() != 0 ){
            java.security.Security.addProvider(new com.sun.net.ssl.internal.ssl.Provider());
            System.setProperty("java.protocol.handler.pkgs", "com.sun.net.ssl.internal.www.protocol");
            System.setProperty("javax.net.ssl.trustStore", truststorePath);
            if( truststorePwd != null && !truststorePwd.trim().equalsIgnoreCase("null") && truststorePwd.trim().length() != 0 ){
                System.setProperty("javax.net.ssl.trustStorePassword", truststorePwd.trim());
            }
        }
        System.out.println(System.getProperty("java.protocol.handler.pkgs"));
        System.out.println(System.getProperty("javax.net.ssl.trustStore"));
        System.out.println(System.getProperty("javax.net.ssl.trustStorePassword"));
    }

My token server already upgraded and we are failing to connect to it.

I edited my code to below to support TLSv1

package ml.token.utility;

import java.io.File;
import java.io.FileInputStream;
import java.net.Socket;
import java.security.KeyStore;  
import javax.net.SocketFactory;  
import javax.net.ssl.KeyManagerFactory;  
import javax.net.ssl.SSLContext;  
import javax.net.ssl.SSLSocketFactory;  
import javax.net.ssl.SSLSocketFactory; 
import javax.net.ssl.SSLSocket; 
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManagerFactory; 
import java.io.IOException;
import java.security.Security;
import org.bouncycastle.crypto.tls.CertificateRequest;
import org.bouncycastle.crypto.tls.DefaultTlsClient;
import org.bouncycastle.crypto.tls.TlsAuthentication;
import org.bouncycastle.crypto.tls.TlsClientProtocol;
import org.bouncycastle.crypto.tls.TlsCredentials;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
// To be used for Ezi SSL certificate //
import java.security.AccessController;
import java.security.PrivilegedExceptionAction;
import java.io.FileNotFoundException;
import java.security.PrivilegedActionException;

public class TrustStore {

    public static void setTruststore(String truststorePath, String truststorePwd)
    {
         KeyStore ks = null;
         FileInputStream fis = null;
        try{
        char[] passwd = null;
    if (truststorePwd.length() != 0)
           passwd = truststorePwd.toCharArray();
        java.security.SecureRandom secureRandom = new java.security.SecureRandom();
        ks = KeyStore.getInstance("JKS");
        fis = new java.io.FileInputStream(truststorePath);
        ks.load(fis, passwd);
        fis.close();
          BouncyCastleProvider bcp = new BouncyCastleProvider();
        if( truststorePath != null && !truststorePath.trim().equalsIgnoreCase("null") && truststorePath.trim().length() != 0 ){
             try {
        Security.addProvider(bcp);
        } catch (Exception e)   {
        throw new RuntimeException("Cannot add BouncyCastle security provider");
        }
             System.setProperty("java.protocol.handler.pkgs", "org.bouncycastle.crypto.tls.TlsClientProtocol");
             System.setProperty("https.protocols", "TLSv1");
             System.setProperty("javax.net.ssl.trustStore", truststorePath);

            if( truststorePwd != null && !truststorePwd.trim().equalsIgnoreCase("null") && truststorePwd.trim().length() != 0 ){
                System.setProperty("javax.net.ssl.trustStorePassword", truststorePwd.trim());
                System.setProperty("javax.net.debug", "ssl");

            final SSLContext context = SSLContext.getInstance("TLS");  
            final TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
                    tmf.init(ks);
            context.init(null, tmf.getTrustManagers(),secureRandom); 

            // get hold of the real underlying socket factory which is expected to do the real job
            final SSLSocketFactory factory = context.getSocketFactory();  
            // "wrap" it around using our custom SSLSocketFactory so that we have control over the enabled protocols of newly created sockets
            final SSLSocketFactory wrappedFactory = new ProtocolOverridingSSLSocketFactory(factory, new String[] {"TLSv1"});
            byte[] ipAddr = new byte[] { 10, 2, 100, 79 };
             SSLSocket socket = (SSLSocket) wrappedFactory.createSocket(java.net.InetAddress.getByAddress(ipAddr), 8443);

            TlsClientProtocol protocol = new TlsClientProtocol(socket.getInputStream(), socket.getOutputStream(),secureRandom);
            DefaultTlsClient client = new DefaultTlsClient() {
                public TlsAuthentication getAuthentication() throws IOException {
                TlsAuthentication auth = new TlsAuthentication() {
                    // Capture the server certificate information!
                    public void notifyServerCertificate(org.bouncycastle.crypto.tls.Certificate serverCertificate) throws IOException {
                    }

                    public TlsCredentials getClientCredentials(CertificateRequest certificateRequest) throws IOException {
                        return null;
                    }
                };
                return auth;
            }
            };
            protocol.connect(client);

       try {

            printSocketInfo(socket);

             socket.startHandshake();
         } catch(Exception e){
             e.printStackTrace();
             System.out.println(e.toString()); 
         }
        finally {
             if (fis != null) {
                 fis.close();
             }
         }
        System.out.println("https.protocols "+System.getProperty("https.protocols"));
        System.out.println("Handler package >> "+System.getProperty("java.protocol.handler.pkgs"));
        System.out.println("truststore>> "+System.getProperty("javax.net.ssl.trustStore"));
        System.out.println("truststore password>> "+System.getProperty("javax.net.ssl.trustStorePassword"));
            }

        }

    }catch(Exception e){
    e.printStackTrace();
    }
}
    private static void printSocketInfo(SSLSocket s) {
        System.out.println("Socket class: "+s.getClass()); 
        System.out.println(" Remote address = " +s.getInetAddress().toString()); 
        System.out.println(" Remote port = "+s.getPort());
        System.out.println(" Local socket address = " +s.getLocalSocketAddress().toString());
        System.out.println(" Local address = " +s.getLocalAddress().toString()); 
        System.out.println(" Local port = "+s.getLocalPort()); System.out.println(" Need client authentication = " +s.getNeedClientAuth()); 
        SSLSession ss = s.getSession();

        System.out.println(" Cipher suite = "+ss.getCipherSuite());
        System.out.println(" Protocol = "+ss.getProtocol()); 
        }

            /**
     * Checks whether a file exists and can be opened.
     * @param file file to be checked.
     * @return FileInputStream to the file or <CODE>null</CODE> when it could
     * not be opened or didn't exist.
     */
    private static FileInputStream getFileInputStream(final File file)  {
    try {
        return AccessController.doPrivileged(
                new PrivilegedExceptionAction<FileInputStream>() {
                    public FileInputStream run() {
                        try {
                            if (file.exists()) {
                                return new FileInputStream(file);
                            } else {
                                return null;
                            }
                        } catch (FileNotFoundException e) {
                            return null;
                        }
                    }
                });
    } catch (PrivilegedActionException e)   {
        // Somehow we cannot run this, hence cannot read the file either...
        return null;
    }
    }
}
