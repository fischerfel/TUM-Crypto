@Singleton
public static class SecureSSLSocketFactoryProvider implements Provider<SSLSocketFactory> {
    private SSLSocketFactory sslSocketFactory;

    public SecureSSLSocketFactoryProvider() throws RuntimeException {
        try {
            final KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
            final InputStream trustStoreFile = new FileInputStream("/usr/share/tomcat7/truststore.jks");
            trustStore.load(trustStoreFile, "changeit".toCharArray());
            final TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            trustManagerFactory.init(trustStore);

            final KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
            final InputStream keyStoreFile = new FileInputStream("/usr/share/tomcat7/keystore.jks");
            keyStore.load(keyStoreFile, "changeit".toCharArray());
            final KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
            keyManagerFactory.init(keyStore, "changeit".toCharArray());

            final SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(keyManagerFactory.getKeyManagers(), trustManagerFactory.getTrustManagers(), null);

            this.sslSocketFactory = sslContext.getSocketFactory();

        } catch (final KeyStoreException e) {
            Log.error("Key store exception: {}", e.getMessage(), e);
        } catch (final CertificateException e) {
            Log.error("Certificate exception: {}", e.getMessage(), e);
        } catch (final UnrecoverableKeyException e) {
            Log.error("Unrecoverable key exception: {}", e.getMessage(), e);
        } catch (final NoSuchAlgorithmException e) {
            Log.error("No such algorithm exception: {}", e.getMessage(), e);
        } catch (final KeyManagementException e) {
            Log.error("Key management exception: {}", e.getMessage(), e);
        } catch (final IOException e) {
            Log.error("IO exception: {}", e.getMessage(), e);
        }
    }

    @Override
    public SSLSocketFactory get() {
        return sslSocketFactory;
    }
}
