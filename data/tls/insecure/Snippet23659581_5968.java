OkHttpClient client = new OkHttpClient();  
KeyStore keyStore = App.getInstance().getKeyStoreUtil().getKeyStore();
TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
tmf.init(keyStore);
KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
kmf.init(keyStore, AppConfig.KEYSTORE_PASSWORD);
SSLContext sslCtx = SSLContext.getInstance("TLS");
sslCtx.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);
client.setSslSocketFactory(sslCtx.getSocketFactory());
