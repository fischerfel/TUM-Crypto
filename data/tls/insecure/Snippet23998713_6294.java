KeyStore keyStore = App.getInstance().getKeyStoreUtil().getKeyStore();
KeyStore trustStore = App.getInstance().getKeyStoreUtil().getTrustStore();

TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
tmf.init(trustStore);

KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
kmf.init(keyStore, AppConfig.KEYSTORE_PASSWORD);


final X509KeyManager origKm = (X509KeyManager) kmf.getKeyManagers()[0];
//it's standard X509KeyManager, I've put some logging there
X509KeyManager km = new MyKeyManager(origKm);

SSLContext sslCtx = SSLContext.getInstance("TLS");
sslCtx.init(new KeyManager[]{km}, tmf.getTrustManagers(), null);
client.setSslSocketFactory(sslCtx.getSocketFactory());
client.setHostnameVerifier(org.apache.http.conn.ssl.SSLSocketFactory.STRICT_HOSTNAME_VERIFIER);
