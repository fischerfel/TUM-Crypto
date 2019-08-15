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
    public String chooseServerAlias(String keyType, 
                                    Principal[] issuers, Socket socket) {
        InetAddress remoteAddress = socket.getInetAddress();
        if (/* remoteAddress has some conditions you need to define yourself */ {
            return "alias1";
        } else {
            return "alias2";
        }
    }

    public String chooseClientAlias(String[] keyType, 
                                    Principal[] issuers, Socket socket) {
       // Delegate this other methods to origKm.
        origKm.chooseClientAlias(keyType, issuers, socket);
    }

    // Delegate this other methods to origKm, in the same way as 
    // it was done for chooseClientAlias.
}

SSLContext sslContext = SSLContext.getInstance("TLS");
sslContext.init(new KeyManager[] { km }, null, null);

SSLSocketFactory sslSocketFactory = sslContext.getSSLSocketFactory();
