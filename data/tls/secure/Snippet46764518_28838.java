public static void configureSSLOnTheClient(WebClient client,
        String keyStoreFileName, String keyStorePassword,
        String trustStoreFileName, String trustStorePassword) {

    HTTPConduit httpConduit = (HTTPConduit) WebClient.getConfig(client).getConduit();
    try {
        TLSClientParameters tlsParams = new TLSClientParameters();

        KeyStore keyStore;
        KeyStore trustStore;
        try {

            keyStore = KeyStore.getInstance("JKS");
            keyStore.load(ClassLoader.getSystemResourceAsStream(keyStoreFileName), keyStorePassword.toCharArray());

            KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
            keyManagerFactory.init(keyStore, keyStorePassword.toCharArray());

            trustStore = KeyStore.getInstance("JKS");
            trustStore.load(ClassLoader.getSystemResourceAsStream(trustStoreFileName), trustStorePassword.toCharArray());

            TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            trustManagerFactory.init(trustStore);

            SSLContext sslContext = SSLContext.getInstance("TLSv1.2");
            sslContext.init(keyManagerFactory.getKeyManagers(), trustManagerFactory.getTrustManagers(), new SecureRandom());

            tlsParams.setSSLSocketFactory(sslContext.getSocketFactory());

        } catch (KeyStoreException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (CertificateException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // These filters ensure that a ciphersuite with export-suitable or null encryption is used,
        // but exclude anonymous Diffie-Hellman key change as this is vulnerable to man-in-the-middle attacks

        FiltersType filters = new FiltersType();
        filters.getInclude().add(".*_EXPORT_.*");
        filters.getInclude().add(".*_EXPORT1024_.*");
        filters.getInclude().add(".*_WITH_DES_.*");
        filters.getInclude().add(".*_WITH_AES_.*");
        filters.getInclude().add(".*_WITH_NULL_.*");
        filters.getExclude().add(".*_DH_anon_.*");

        tlsParams.setCipherSuitesFilter(filters);

        httpConduit.setTlsClientParameters(tlsParams);

    } catch (Exception exception) {
        LOGGER.error("Security configuration failed with the following: " + exception.getCause(), exception);
    }
}
