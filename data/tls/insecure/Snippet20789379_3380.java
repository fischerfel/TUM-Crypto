KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
keystore.load(new FileInputStream(new File("keystoreCompletePath")), "passwdKeyStore");
TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
tmf.init(keyStore);
SSLContext ctx = SSLContext.getInstance("TLS");
ctx.init(null, tmf.getTrustManagers(), null);
SSLSocketFactory sslFactory = ctx.getSocketFactory();
