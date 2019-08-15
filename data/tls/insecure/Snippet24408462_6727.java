OkHttpClient client = new OkHttpClient();
KeyStore keyStore = App.getInstance().getKeyStoreUtil().getKeyStore();
KeyStore trustStore = App.getInstance().getKeyStoreUtil().getTrustStore();

TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
tmf.init(trustStore);

KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
kmf.init(keyStore, keyStorePassword);

SSLContext sslCtx = SSLContext.getInstance("TLS");
sslCtx.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);
client.setSslSocketFactory(sslCtx.getSocketFactory());
client.setHostnameVerifier(org.apache.http.conn.ssl.SSLSocketFactory.STRICT_HOSTNAME_VERIFIER);
