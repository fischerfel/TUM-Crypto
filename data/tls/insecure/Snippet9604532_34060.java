   java.security.KeyStore ks = java.security.KeyStore.getInstance("JKS");
   java.security.KeyStore ts = java.security.KeyStore.getInstance("JKS");

   ks.load(new java.io.FileInputStream(keyStoreFile), passphrase);
   ts.load(new java.io.FileInputStream(trustStoreFile), passphrase);

   KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
   kmf.init(ks, passphrase);

   TrustManagerFactory tmf = TrustManagerFactory.getInstance("SunX509");
   tmf.init(ts);

   SSLContext sslc = SSLContext.getInstance("TLS");     
   sslc.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);


   SSLEngine serverEngine = sslc.createSSLEngine();
   serverEngine.setUseClientMode(false);
   serverEngine.setEnableSessionCreation(true);
   serverEngine.setWantClientAuth(true);
