// Load the key store: change store type if needed
KeyStore ks = KeyStore.getInstance(KeyStore.getDefaultType());
FileInputStream fis = new FileInputStream("/path/to/keystore");
try {
    ks.load(fis, keystorePassword);
} finally {
    if (fis != null) { fis.close(); }
}

// Get the default Key Manager
KeyManagerFactory kmf = KeyManagerFactory.getInstance(
   KeyManagerFactory.getDefaultAlgorithm());
kmf.init(ks, keyPassword);

final X509KeyManager origKm = (X509KeyManager)kmf.getKeyManagers()[0];
X509KeyManager km = new X509KeyManager() {
    public String chooseClientAlias(String[] keyType, 
                                    Principal[] issuers, Socket socket) {
        // Implement your alias selection, possibly based on the socket
        // and the remote IP address, for example.
    }

    // Delegate the other methods to origKm.
}

SSLContext sslContext = SSLContext.getInstance("TLS");
sslContext.init(new KeyManager[] { km }, null, null);

SSLSocketFactory sslSocketFactory = sslContext.getSSLSocketFactory();
