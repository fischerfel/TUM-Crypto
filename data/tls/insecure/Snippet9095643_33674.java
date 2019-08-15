tmf = TrustManagerFactory.getInstance("SunX509");
kmf = KeyManagerFactory.getInstance("SunX509");
tmf.init(trustKeyStore);
kmf.init(keyStore, password);

// create, init SSLContext
    SSLContext sslCtx = SSLContext.getInstance("TLS");
    sslCtx.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);
