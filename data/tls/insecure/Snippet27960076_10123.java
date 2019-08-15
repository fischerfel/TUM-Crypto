 public static void main(final String[] args) throws Exception {
    final SSLContext sslContext = SSLContext.getInstance("TLSv1");
    sslContext.init(null, null, null);
    // getDefault();
    final SSLSocketFactory fac = sslContext.getSocketFactory();
    final SSLSocket socket = (SSLSocket) fac.createSocket("google.de", 443);

    socket.addHandshakeCompletedListener(new HandshakeCompletedListener() {

        @Override
        public void handshakeCompleted(final HandshakeCompletedEvent event) {
        System.out.println("Cipher:" + event.getCipherSuite());
        }
    });
    final String[] ciphers = fac.getSupportedCipherSuites();
    final String[] protocols = { "TLSv1" };
    final SSLParameters params = new SSLParameters(ciphers, protocols);
    params.setNeedClientAuth(false);
    socket.setSSLParameters(params);
    socket.startHandshake();
 }
