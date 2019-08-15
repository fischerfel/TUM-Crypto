    SSLContext sslContext = SSLContext.getInstance("SSL");
    sslContext.init(null, NetUtility.getAllTrusting(), new SecureRandom());
    SSLServerSocketFactory factory = sslContext.getServerSocketFactory();
    SSLServerSocket socket = (SSLServerSocket) factory.createServerSocket(this.server.getPort());
    socket.setUseClientMode(false);
    socket.setSoTimeout(500);
