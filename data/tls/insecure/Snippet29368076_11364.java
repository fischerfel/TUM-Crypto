KeyStore keyStore = KeyStore.getInstance("JKS");
keyStore.load(...);
TrustManagerFactory tmf = TrustManagerFactory.getInstance("PKIX");
tmf.init(keyStore);
SSLContext sslctx = SSLContext.getInstance("SSL");
sslctx.init(null, tmf.getTrustManagers(), null);
HttpsURLConnection.setDefaultSSLSocketFactory(sslctx.getSocketFactory());
