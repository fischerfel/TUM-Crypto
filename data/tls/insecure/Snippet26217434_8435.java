public class ClientCertSocketFactory implements SecureProtocolSocketFactory {
    private static final Logger LOGGER = LoggerFactory.getLogger(ClientCertSocketFactory.class);

    private SSLSocketFactory sslSocketFactory;

    public ClientCertSocketFactory() throws IOException{
        String trustStoreFilePath = System.getProperty("javax.net.ssl.trustStore");
        try {
            TrustManagerFactory trustManagerFactory = CertManagerFactory.loadTrustStore(trustStoreFilePath);
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, trustManagerFactory.getTrustManagers(), null);
            this.sslSocketFactory = sslContext.getSocketFactory();
        } catch (NoSuchAlgorithmException e) {
            LOGGER.error("No Provider supports a TrustManagerFactorySpi implementation for the TLS protocol. Error message: " + e);
        } catch (KeyManagementException e) {
            LOGGER.error("Error occurred when initializing SSLContext. Error message: " + e);
        }
    }
}
