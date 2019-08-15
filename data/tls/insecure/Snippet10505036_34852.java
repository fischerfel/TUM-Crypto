private boolean handleSSLHandshake() {

    try {
        SSLContext sc = SSLContext.getInstance("TLS");
        sc.init(this.kmf.getKeyManagers(), null, null);
        SSLSocketFactory factory = sc.getSocketFactory();            
        this.sslSocket = (SSLSocket) factory.createSocket(this.socket,
                                         this.socket.getInetAddress().getHostAddress(),
                                         this.socket.getPort(),
                                         true);
        this.sslSocket.setUseClientMode(false);
        this.sslSocket.addHandshakeCompletedListener(this);
        this.sslSocket.startHandshake();
        Log.d(TAG, "SSL upgrade succeeds!");
        this.input = this.sslSocket.getInputStream();
        this.output = this.sslSocket.getOutputStream();
    } catch (NoSuchAlgorithmException e) {
        Log.d(TAG, "Got NoSuchAlgorithmException while upgrading to SSL" + e);
        this.sslSocket = null;
        return false;
    } catch (KeyManagementException e) {
        Log.d(TAG, "Got KeyManagementException while upgrading to SSL" + e);
    } catch (UnknownHostException e) {
        Log.d(TAG, "Got UnknownHostException while upgrading to SSL" + e);
        this.sslSocket = null;
        return false;
    } catch (IOException e) {
        Log.d(TAG, "Got IOException while upgrading to SSL" + e);
        this.sslSocket = null;
        return false;
    }
    return true;
}

public void handshakeCompleted(HandshakeCompletedEvent event) {

    Log.d(TAG, "SSL handshake completed");
}
