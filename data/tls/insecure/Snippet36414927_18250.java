    SSLContext sslContext = SSLContext.getInstance("SSL");
    sslContext.init(null, NetUtility.getAllTrusting(), new SecureRandom());
    SSLSocketFactory factory = sslContext.getSocketFactory();
    SSLSocket socket = (SSLSocket) factory.createSocket(this.client.getTargetHost(), this.client.getTargetPort());
    socket.setUseClientMode(true);
    socket.startHandshake();
