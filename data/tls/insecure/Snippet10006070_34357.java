    SSLContext sslcontext = SSLContext.getInstance("TLS");
    sslcontext.init(getKeyManagers(), {new DummyTrustManager()}, null);
ServerSocketFactory   mFactory = sslcontext.getServerSocketFactory();
ServerSocket serverSocket =mFactory.createServerSocket(8080);
SSLSocket clientSocket = serverSocket.accept();
socket.getSession().getLocalCertificates();
socket.getSession().getPeerHost();
socket.getSession().getPeerPort();
socket.getSession().getPeerCertificates();`
