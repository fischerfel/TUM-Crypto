InputStream stream = new ByteArrayInputStream(pksData);         
KeyStore keyStore = KeyStore.getInstance("PKCS12");
keyStore.load(stream, password);

KeyManagerFactory kmf = KeyManagerFactory.getInstance(
    KeyManagerFactory.getDefaultAlgorithm());
kmf.init(keyStore, password.toCharArray());
KeyManager[] keyManagers = kmf.getKeyManagers();

TrustManagerFactory tmfactory = TrustManagerFactory.getInstance(
    TrustManagerFactory.getDefaultAlgorithm());
tmfactory.init(keyStore);
TrustManager[] trustManagers = tmfactory.getTrustManagers();

SSLContext sslContext = SSLContext.getInstance("TLS");
sslContext.init(keyManagers, trustManagers, null);
sslSocketFactory = sslContext.getSocketFactory();
