KeyStore cKeyStore = KeyStore.getInstance("PKCS12");
try (InputStream clientCertKeyInput = new FileInputStream("my.pfx")) {
     cKeyStore.load(clientCertKeyInput, "password".toCharArray());
}
KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
keyManagerFactory.init(cKeyStore, "password".toCharArray());

SSLContext sslCtx = SSLContext.getInstance("TLS");
sslCtx.init(keyManagerFactory.getKeyManagers(), 
            null, // default javax.net.ssl.trustStore
            new SecureRandom()); 

SSLSocketFactory sslSocketFactory = sslCtx.getSocketFactory();
