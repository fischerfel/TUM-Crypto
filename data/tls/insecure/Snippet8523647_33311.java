boolean connected = false;
if (socket == null || socket.isClosed() || !socket.isConnected()) {
    if (socket != null && !socket.isClosed()) {
        socket.close();
    }

    Log.i(getClass().toString(), "Connecting...");
    messages.getText().append("Connecting...");
    final KeyStore keyStore = KeyStore.getInstance("BKS");
    keyStore.load(getResources().openRawResource(R.raw.serverkey), null);

    final KeyManagerFactory keyManager = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
    keyManager.init(keyStore, null);
    //keyManager.init(null, null);

    final TrustManagerFactory trustFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
    trustFactory.init(keyStore);

    sslContext = SSLContext.getInstance("TLS");
    sslContext.init(keyManager.getKeyManagers(), trustFactory.getTrustManagers(), rnd);
    final SSLSocketFactory delegate = sslContext.getSocketFactory();
    SocketFactory factory = new SSLSocketFactory() {
        @Override
        public Socket createSocket(String host, int port)
                        throws IOException, UnknownHostException {

            InetAddress addr = InetAddress.getByName(host);
            injectHostname(addr, host);
            return delegate.createSocket(addr, port);
        }
        @Override
        public Socket createSocket(InetAddress host, int port)
                        throws IOException {

            return delegate.createSocket(host, port);
        }
        @Override
        public Socket createSocket(String host, int port, InetAddress localHost, int localPort)
                        throws IOException, UnknownHostException {

            return delegate.createSocket(host, port, localHost, localPort);
        }
        @Override
        public Socket createSocket(InetAddress address, int port, InetAddress localAddress, int localPort)
                        throws IOException {

            return delegate.createSocket(address, port, localAddress, localPort);
        }
        private void injectHostname(InetAddress address, String host) {
            try {
                Field field = InetAddress.class.getDeclaredField("hostName");
                field.setAccessible(true);
                field.set(address, host);
            } catch (Exception ignored) {
            }
        }
        @Override
        public Socket createSocket(Socket s, String host, int port, boolean autoClose) throws IOException {

            injectHostname(s.getInetAddress(), host);
            return delegate.createSocket(s, host, port, autoClose);
        }
        @Override
        public String[] getDefaultCipherSuites() {
            return delegate.getDefaultCipherSuites();
        }
        @Override
        public String[] getSupportedCipherSuites() {
            return delegate.getSupportedCipherSuites();
        }
    };
    socket = (SSLSocket)factory.createSocket("192.168.197.133", 9999);
    socket.setSoTimeout(20000);
    socket.setUseClientMode(true);
    connected = true;
    Log.i(getClass().toString(), "Connected.");
    messages.getText().append("Connected.");
}

// Secure
if (connected) {
    Log.i(getClass().toString(), "Securing...");
    messages.getText().append("Securing...");
    SSLSession session = socket.getSession();
    boolean secured = session.isValid();
    if (secured) {
        Log.i(getClass().toString(), "Secured.");
        messages.getText().append("Secured.");
    }
}
