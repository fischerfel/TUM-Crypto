private void registerKeyStore(String keyStoreName) {
    try {
        ClassLoader classLoader = this.getClass().getClassLoader();
        InputStream keyStoreInputStream = classLoader.getResourceAsStream(keyStoreName);
        if (keyStoreInputStream == null) {
            throw new FileNotFoundException("Could not find file named '" + keyStoreName + "' in the CLASSPATH");
        }

        //load the keystore
        KeyStore keystore = KeyStore.getInstance(KeyStore.getDefaultType());
        keystore.load(keyStoreInputStream, null);

        //add to known keystore 
        TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        trustManagerFactory.init(keystore);

        //default SSL connections are initialized with the keystore above
        TrustManager[] trustManagers = trustManagerFactory.getTrustManagers();
        SSLContext sc = SSLContext.getInstance("SSL");
        sc.init(null, trustManagers, null);
        SSLContext.setDefault(sc);
    } catch (IOException | GeneralSecurityException e) {
        throw new RuntimeException(e);
    }
}
