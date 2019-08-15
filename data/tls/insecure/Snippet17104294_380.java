class SslSessionContextFactory
{
    SSLContext sslContext;

    public SslSessionContextFactory(SslInfo info) throws Exception
    {
        KeyStore store = KeyStore.getInstance(info.getKeyStoreType());

        // Obtain the input stream for the key <--- this is the code in question.
        store.load(info.newKeyStream(), info.getPassphrase().toCharArray());

        TrustManagerFactory factory = TrustManagerFactory.getInstance(
                                          TrustManagerFactory.getDefaultAlgorithm());
        factory.init(store);

        // Initialize the SSL context.
        sslContext = SSLContext.getInstance("TLS");
        sslContext.init(null, getCertificates(), new SecureRandom());
    }

    public SSLSessionContext getSessionContext()
    {
        // Initialize the session context.
        SSLSessionContext sessionContext = sslContext.getServerSessionContext();
        sessionContext.setSessionCacheSize(SESSION_CACHE_SIZE);
        sessionContext.setSessionTimeout(SESSION_TIMEOUT);
        return sessionContext;
    }
}
