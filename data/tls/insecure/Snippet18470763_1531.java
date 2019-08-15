/**
 * Configure mutal TLS on WS given in parameter
 * 
 * @param portType webservice
 * @throws Exception
 */
public static void setTLS(Object ws) throws Exception {

    if (tlsClientParameters == null) {
        if (StringUtils.hasLength(ConfigUtils.getInstance().getProxyHost()) && StringUtils.hasLength(ConfigUtils.getInstance().getProxyPort())) {
            System.setProperty("https.proxyHost", ConfigUtils.getInstance().getProxyHost());
            System.setProperty("https.proxyPort", ConfigUtils.getInstance().getProxyPort());
        }

        X509Certificate[] certificatesChain = new X509Certificate[1];

        KeyManager[] managers = new KeyManager[1];

        Pkcs12 pkcs12 = new Pkcs12();
        pkcs12.login(new ClassPathResource(ConfigUtils.getInstance().getCertificateFileName()).getURI().getPath(), ConfigUtils.getInstance().getCertificatePassword());
        certificatesChain[0] = pkcs12.getCertificate();
        managers[0] = new SSLKeyManagers(certificatesChain, pkcs12.getKey());

        Client client = ClientProxy.getClient(portType);
        SSLContext sslContext = SSLContext.getInstance("TLS");

        TrustManager[] trustManager = getServerTrustManager();

        sslContext.init(managers, trustManager, null);
        HTTPConduit conduit = (HTTPConduit) client.getConduit();

        tlsClientParameters = new TLSClientParameters();
        if (trustManager != null) {
            tlsClientParameters.setTrustManagers(trustManager);
        }
        tlsClientParameters.setKeyManagers(managers);
        tlsClientParameters.setSSLSocketFactory(sslContext.getSocketFactory());

        tlsClientParameters.setDisableCNCheck(false);

        conduit.setTlsClientParameters(tlsClientParameters);

    } else {

        Client client = ClientProxy.getClient(portType);
        HTTPConduit conduit = (HTTPConduit) client.getConduit();

        conduit.setTlsClientParameters(tlsClientParameters);
    }
}

/**
 * 
     * Gets the trust manager
 *  
 * @return keystore
 * @throws NoSuchAlgorithmException
 * @throws KeyStoreException
 * @throws ResourceException 
 * @throws IOException 
 * @throws CertificateException 
 */
private static TrustManager[] getServerTrustManager() throws NoSuchAlgorithmException, KeyStoreException, CertificateException, IOException, ResourceException {

    TrustManagerFactory factory = TrustManagerFactory.getInstance("X.509");

    KeyStore trustStore = KeyStore.getInstance("JKS");

    trustStore.load(new ClassPathResource(ConfigUtils.getInstance().getServerKeyStoreFileName()).getInputStream(), ConfigUtils.getInstance().getServerKeystorePassword().toCharArray());

    factory.init(trustStore);       
    return factory.getTrustManagers();
}
