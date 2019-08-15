    //load client private key
    KeyStore clientKeys = KeyStore.getInstance("JKS");
    clientKeys.load(new FileInputStream("proxyKeystore"),"password".toCharArray());
    KeyManagerFactory clientKeyManager = KeyManagerFactory.getInstance("SunX509");
    clientKeyManager.init(clientKeys,"password".toCharArray());
    //load server public key
    KeyStore serverPub = KeyStore.getInstance("JKS");
    serverPub.load(new FileInputStream("proxyTrustedStore"),"password".toCharArray());
    TrustManagerFactory trustManager = TrustManagerFactory.getInstance("SunX509");
    trustManager.init(serverPub);

  //use keys to create SSLSoket
  SSLContext ssl = SSLContext.getInstance("TLS");
  ssl.init(clientKeyManager.getKeyManagers(), trustManager.getTrustManagers(), SecureRandom.getInstance("SHA1PRNG"));
  socket = (SSLSocket)ssl.getSocketFactory().createSocket("localhost", 8889);


  socket.startHandshake();
