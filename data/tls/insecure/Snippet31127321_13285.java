public class DefaultSSLContextProvider implements SSLContextProvider
{
    private SSLContext sslContext;
    private ObservableSSLSocketFactory observableSSLSocketFactory;

    private static final Logger logger = LoggerFactory.getLogger(DefaultSSLContextProvider.class);

    public DefaultSSLContextProvider()
    {
        try
        {
            TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance("X509");
            sslContext = SSLContext.getInstance("SSL");
            KeyStore keyStore = getKeyStore();
            trustManagerFactory.init(keyStore);
            sslContext.init(null, trustManagerFactory.getTrustManagers(), new SecureRandom());
            observableSSLSocketFactory = new ObservableSSLSocketFactory(sslContext);
            HttpsURLConnection.setDefaultSSLSocketFactory(observableSSLSocketFactory);
            SSLContext.setDefault(sslContext);
        }
        catch (NoSuchAlgorithmException e)
        {
            throw new RuntimeException(e);
        }
        catch (KeyManagementException | KeyStoreException e)
        {
            logger.error("could not create DefaultSSLContextProvider", e);
            throw new IllegalStateException(e);
        }
    }

    @Override
    public SSLContext getSSLContext()
    {
        return sslContext;
    }

    @Override
    public SSLSocketFactory getSSLSocketFactory()
    {
        return observableSSLSocketFactory;
    }

    @Override
    public KeyStore getKeyStore()
    {
        // snip
    }
}
