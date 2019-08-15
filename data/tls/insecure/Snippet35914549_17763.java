//pass a p12 or pfx file (file may be on classpath also)
public void initSSL(String keyStoreFile, String pass) {
        InputStream keyStoreStream = this.getClass().getClassLoader().getResourceAsStream(keyStoreFile);          

            KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
            KeyStore keyStore = KeyStore.getInstance("PKCS12");

            keyStore.load(keyStoreStream, keyPassword.toCharArray());
            kmf.init(keyStore, keyPassword.toCharArray());

             KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
            trustStore.load(null, null);

            // init the trust manager factory by read certificates
            TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            tmf.init(trustStore);

            // 3. init the SSLContext using kmf and tmf above
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);
            SSLContext.setDefault(sslContext);
}
