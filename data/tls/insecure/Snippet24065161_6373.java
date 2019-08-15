// Load the key store: change store type if needed
KeyStore ks = KeyStore.getInstance(KeyStore.getDefaultType());
FileInputStream fis = new FileInputStream("/path/to/keystore");
try {
    ks.load(fis, keystorePassword);
} finally {
    if (fis != null) { fis.close(); }
}

SSLContext sslContext = SSLContext.getInstance("TLS");
// the second "null" argument will let you use the default trust manager settings.
sslContext.init(kmf.getKeyManagers(), null, null);

SSLSocketFactory sslSocketFactory = sslContext.getSSLSocketFactory();
