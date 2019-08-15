public class SSLHelper
{

   ...

    /**
     * Initialisiert die Client-SocketFactory.
     */
    private void initClient()
        throws KeyStoreException, NoSuchAlgorithmException,
               KeyManagementException, IOException,
               CertificateException
    {
        // die Namen für getInstance() sind aus diesem Dokument:
        //   http://java.sun.com/javase/6/docs/technotes/guides/security/StandardNames.html
        KeyStore keystore = KeyStore.getInstance("jks");
        keystore.load(SSLHelper.class
                      .getResourceAsStream("client-keystore.jks"),
                      null);
        TrustManagerFactory tmf = TrustManagerFactory.getInstance("PKIX");
        tmf.init(keystore);
        SSLContext kontext = SSLContext.getInstance("TLS");
        kontext.init(null, tmf.getTrustManagers(), null);

        clientFactory = kontext.getSocketFactory();
    }
}
