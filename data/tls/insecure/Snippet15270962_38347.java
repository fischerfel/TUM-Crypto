String provider = System.getProperty("javax.net.ssl.keyStoreProvider");
String keystoreType = System.getProperty("javax.net.ssl.keyStoreType", KeyStore.getDefaultType());
KeyStore ks = null;
if (provider != null) {
    ks = KeyStore.getInstance(keystoreType, provider);
} else {
    ks = KeyStore.getInstance(keystoreType);
}
InputStream ksis = null;
String keystorePath = System.getProperty("javax.net.ssl.keyStore");
String keystorePassword = System.getProperty("javax.net.ssl.keyStorePassword");
if (keystorePath != null && !"NONE".equals(keystorePath)) {
    ksis = new FileInputStream(keystorePath);
}
try {
    ks.load(ksis, keystorePassword.toCharArray());
} finally {
     if (ksis != null) { ksis.close(); }
}

KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
kmf.init(ks, keystorePassword.toCharArray());
// Note that there is no property for the key password itself, which may be different.
// We're using the keystore password too.

SSLContext sslContext = SSLContext.getInstance("TLS");
sslContext.init(kmf.getKeyManagers(), ..., null);
