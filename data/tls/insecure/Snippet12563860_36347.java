SSLContext context = SSLContext.getInstance("TLS");
TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
[...]
SavingTrustManager tm = new SavingTrustManager(defaultTrustManager);
SSLSocket socket = (SSLSocket)factory.createSocket(host, port);
try {
    socket.startHandshake();
    socket.close();
} catch (SSLException e) {
    e.printStackTrace(System.out);
}
