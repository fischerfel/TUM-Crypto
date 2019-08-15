KeyStore keyStore = KeyStore.getInstance("JKS");
keyStore.load(getResources().openRawResource(R.raw.mykeystore), "MY_PASSWORD".toCharArray());

TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
trustManagerFactory.init(keyStore);

SSLContext sslctx = SSLContext.getInstance("TLS");
sslctx.init(null, trustManagerFactory.getTrustManagers(), new SecureRandom());

SSLSocketFactory factory = sslctx.getSocketFactory();
