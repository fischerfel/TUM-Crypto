KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
kmf.init(keyStore, password);

final X509KeyManager origKm = (X509KeyManager) kmf.getKeyManagers()[0];
X509KeyManager km = new MyKeyManager(origKm);

SSLContext sslCtx = SSLContext.getInstance("TLS");
sslCtx.init(new KeyManager[]{km}, tmf.getTrustManagers(), null);
