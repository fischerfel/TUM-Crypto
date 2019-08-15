public static SSLContext setupClientSSL(String certPath, String certPassword)
        throws SSLConfigurationException {
    SSLContext sslContext = null;
    try {
        KeyManagerFactory kmf =
                KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());

        KeyStore ks = p12ToKeyStore(certPath, certPassword);
        kmf.init(ks, certPassword.toCharArray());
        //sslContext = getSSLContext(kmf.getKeyManagers());
        sslContext = SSLContext.getInstance("SSL", "IBMJSSE2");
    } catch (NoSuchAlgorithmException e) {
        throw new SSLConfigurationException(e.getMessage(), e);
    } catch (KeyStoreException e) {
        throw new SSLConfigurationException(e.getMessage(), e);
    } catch (UnrecoverableKeyException e) {
        throw new SSLConfigurationException(e.getMessage(), e);
    } catch (CertificateException e) {
        throw new SSLConfigurationException(e.getMessage(), e);
    } catch (NoSuchProviderException e) {
        throw new SSLConfigurationException(e.getMessage(), e);
    } catch (IOException e) {
        throw new SSLConfigurationException(e.getMessage(), e);
    }
    return sslContext;
}
