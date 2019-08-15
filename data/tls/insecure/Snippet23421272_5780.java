    private SSLServerSocket getSslServerSocket(int port)
        throws GeneralSecurityException {
    KeyManager[] keyManager = this.mKeyStoreManager.getKeyManagers();
    TrustManager[] trustManager = this.mKeyStoreManager.getTrustManagers();
    SSLContext sslContext = SSLContext.getInstance("TLS");
    sslContext.init(keyManager, trustManager, (SecureRandom) null);
    SSLServerSocket sslServerSocket = null;
    try {

        sslServerSocket = (SSLServerSocket) sslContext
                .getServerSocketFactory().createServerSocket(port);
        sslServerSocket.setNeedClientAuth(true);
        sslServerSocket.setUseClientMode(false);
    } catch (Exception ex) {
        Log.e("----Exception", ex.getMessage());
    }

    return sslServerSocket;
}
