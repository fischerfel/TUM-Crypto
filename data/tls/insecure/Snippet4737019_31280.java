// Connect
if (socket == null || socket.isClosed() || !socket.isConnected()) {
    if (socket != null && !socket.isClosed())
        socket.close();
    Log.i(getClass().toString(), "Connecting...");
    if (sslContext == null) {
        sslContext = SSLContext.getInstance("TLS");
        sslContext.init(null, trustAllCerts, new SecureRandom()); 
    }
    SSLSocketFactory socketFactory = sslContext.getSocketFactory();
    socket = (SSLSocket)socketFactory.createSocket(host, port);
    socket.setSoTimeout(20000);
    socket.setUseClientMode(true);
    connected = true;
    Log.i(getClass().toString(), "Connected.");
}

// Secure
if (connected) {
    Log.i(getClass().toString(), "Securing...");
    SSLSession session = socket.getSession();
    secured = session.isValid();
    if (secured) {
        Log.i(getClass().toString(), "Secured.");
    }
    else
        Log.i(getClass().toString(), "Securing failed.");
}
