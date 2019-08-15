TrustManagerFactory tmf = TrustManagerFactory
    .getInstance(TrustManagerFactory.getDefaultAlgorithm());
// Using null here initialises the TMF with the default trust store.
tmf.init((KeyStore) null);

// Get hold of the default trust manager
X509TrustManager defaultTm = null;
for (TrustManager tm : tmf.getTrustManagers()) {
    if (tm instanceof X509TrustManager) {
        defaultTm = (X509TrustManager) tm;
        break;
    }
}

FileInputStream myKeys = new FileInputStream("truststore.jks");

// Do the same with your trust store this time
// Adapt how you load the keystore to your needs
KeyStore myTrustStore = KeyStore.getInstance(KeyStore.getDefaultType());
myTrustStore.load(myKeys, "password".toCharArray());

myKeys.close();

tmf = TrustManagerFactory
    .getInstance(TrustManagerFactory.getDefaultAlgorithm());
tmf.init(myTrustStore);

SSLContext sslContext = SSLContext.getInstance("TLS");
sslContext.init(null, tmf.getTrustManagers(), null);
