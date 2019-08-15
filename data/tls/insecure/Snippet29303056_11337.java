    public void startServer() {
    threadPool = (ThreadPoolExecutor) Executors.newCachedThreadPool();
    threadPool.setCorePoolSize(20);

    try {
        SSLContext sslContext = getSSLContext();
  SSLServerSocketFactory ssf = sslContext.getServerSocketFactory();
  SSLServerSocket serverSocket = (SSLServerSocket) ssf.createServerSocket(8888);
        String[] ciphers = {"TLS_ECDHE_ECDSA_WITH_AES_128_CBC_SHA256"};
  serverSocket.setEnabledCipherSuites(ciphers);
  serverSocket.setEnabledProtocols(new String[] {"TLSv1.2"});

  while (true) {
    SSLSocket clientSocket = (SSLSocket) serverSocket.accept();
    threadPool.execute(new ClientSession(clientSocket));
  }

    } catch (Exception e) {
        e.printStackTrace();
    }
}

    protected SSLContext getSSLContextForGWA() throws Exception {
    SSLContext sslContext = SSLContext.getInstance("TLS");
    try {
        KeyStore ks = KeyStore.getInstance("PKCS12", "BC");
        File keyFile = new File(System.getProperty("user.dir") + File.separator + "certs" + File.separator + "server" + File.separator + "server-cert.p12");
        FileInputStream keyInput = new FileInputStream(keyFile);
        ks.load(keyInput, keyStorePassword.toCharArray());
        keyInput.close();

        // Set up key manager factory to use our key store
        KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
        kmf.init(ks, keyStorePassword.toCharArray());

        // Initialize the SSLContext to work with our key managers.
        sslContext.init(kmf.getKeyManagers(), null, null);

        return sslContext;

    } catch (Exception e) {
        e.printStackTrace();
    }

    return null;
}
