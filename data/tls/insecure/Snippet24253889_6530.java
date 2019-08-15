public void client() throws UnknownHostException, IOException{
    KeyStore keyStore = KeyStore.getInstance("PKCS12");
    FileInputStream stream = new FileInputStream(new File("")); // need correct file
    keyStore.load(stream, "Some Password".toCharArray());
    // load in the appropriate keystore and truststore for the client
    // get the X509KeyManager and X509TrustManager instances
    TrustManagerFactory trustManagerFactory =
            TrustManagerFactory.getInstance("PKIX", "SunJSSE");
        trustManagerFactory.init("NOT SURE WHAT TO PUT HERE");


    SSLContext sslContext = SSLContext.getInstance("TLS");

    sslContext.init(new KeyManager[]{"NOT SURE WHAT TO PUT HERE"},
        new TrustManager[]{"NOT SURE WHAT TO PUT HERE"}, null);

    SSLSocketFactory socketFactory = sslContext.getSocketFactory();
    SSLSocket socket =
        (SSLSocket) socketFactory.createSocket("localhost", 25500);

    socket.setEnabledProtocols(new String[]{"TLSv1"});

    // read from the socket, etc
}

public void server() throws IOException{
    // load in the appropriate keystore and truststore for the server
    // get the X509KeyManager and X509TrustManager instances

    SSLContext sslContext = SSLContext.getInstance("TLS");
    // the final null means use the default secure random source
    sslContext.init(new KeyManager[]{"NOT SURE WHAT TO PUT HERE"},
        new TrustManager[]{"NOT SURE WHAT TO PUT HERE"}, null);

    SSLServerSocketFactory serverSocketFactory =
        sslContext.getServerSocketFactory();
    SSLServerSocket serverSocket =
        (SSLServerSocket) serverSocketFactory.createServerSocket(25500);

    serverSocket.setNeedClientAuth(true);
    // prevent older protocols from being used, especially SSL2 which is insecure
    serverSocket.setEnabledProtocols(new String[]{"TLSv1"});

    // you can now call accept() on the server socket, etc
}
