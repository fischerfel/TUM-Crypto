KeyStore ks = KeyStore.getInstance(...); // Load the keystore
ks.load(...); // Load as required from the inputstream of your choice, for example.

KeyStore ts = KeyStore.getInstance(...); // Load the truststore
ts.load(...);

KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
kmf.init(ks, <the key password>);

TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
tmf.init(ts);

SSLContext sslContext = SSLContext.getInstance("TLS");
sslContext.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);
