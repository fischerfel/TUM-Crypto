class ATrustManager implements X509TrustManager { 
  public ATrustManager() {} 
  public void checkClientTrusted(X509Certificate[] certs, String authType) {} 
  public void checkServerTrusted(X509Certificate[] certs, String authType) {}

  // --- What!? ---
  public X509Certificate[] getAcceptedIssuers() {
    java.security.cert.X509Certificate[0]; 
  }
  // --------------
}

class Blah {
  SomeObject doBlah(...) {  
    // ... various code ...

    char[] password = "password".toCharArray();         
    KeyStore keystore = KeyStore.getInstance("JKS");
    keystore.load(FileInputStream("app.keys"), password);
    KeyManagerFactory aKeyManagerFactory =
        KeyManagerFactory.getInstance("SunX509");   
    aKeyManagerFactory.init(keystore, password);
    KeyManager[] aKeyManager = aKeyManagerFactory.getKeyManagers();
    TrustManager[] aTrustManager = new TrustManager[] { new ATrustManager() };
    SSLContext sslcontext = SSLContext.getInstance("SSL");
    sslcontext.init(aKeyManager, aTrustManager, null);

    SSLSocketFactory socketFactory = sslcontext.getSocketFactory();
    Socket socket = socketFactory.createSocket(hostname, port);
    OutputStream out = socket.getOutputStream();

    // ... various code ...     
}
