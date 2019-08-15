OkHttpClient client = new OkHttpClient();
KeyStore keyStore = readKeyStore(); //your method to obtain KeyStore
SSLContext sslContext = SSLContext.getInstance("SSL");
TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
trustManagerFactory.init(keyStore);
KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
keyManagerFactory.init(keyStore, "keystore_pass".toCharArray());
sslContext.init(keyManagerFactory.getKeyManagers(),trustManagerFactory.getTrustManagers(), new SecureRandom());
client.setSslSocketFactory(sslContext.getSocketFactory());
