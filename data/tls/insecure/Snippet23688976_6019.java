System.setProperty("javax.net.ssl.trustStore", "cfgFiles/trustedHttpsCertificates.truestore");
System.setProperty("javax.net.ssl.trustStoreType", "jks");
System.setProperty("javax.net.ssl.trustStorePassword", "oiadad");

Provider newProvider = new SunPKCS11("cfgFiles/etpkcs11.cfg");
Security.addProvider(newProvider);

try {
    KeyStore keyStore = KeyStore.getInstance("PKCS11");
    keyStore.load(null, "".toCharArray());

    KeyManagerFactory keyFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
    keyFactory.init(keyStore,null);

    SSLContext sslContext = SSLContext.getInstance("SSL", "SunJSSE");
    sslContext.init(keyFactory.getKeyManagers(), null, null);
    sslSocketFactory = sslContext.getSocketFactory();

} catch (KeyStoreException e) {
    e.printStackTrace();
} catch (NoSuchAlgorithmException e) {
    e.printStackTrace();
} catch (CertificateException e) {
    e.printStackTrace();
} catch (IOException e) {
    e.printStackTrace();
} catch (NoSuchProviderException e) {
    e.printStackTrace();
} catch (UnrecoverableKeyException e1) {
    e1.printStackTrace();
} catch (KeyManagementException e1) {
    e1.printStackTrace();
}
