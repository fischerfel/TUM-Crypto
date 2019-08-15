public class MySSLProtocolSocketFactory implements SecureProtocolSocketFactory {

    private SSLContext sslContext = null;

    public MySSLProtocolSocketFactory() {
        super();
    }

    private static SSLContext createMySSLContext() {
        try {
            MyX509TrustManager myX509TrustManager = new MyX509TrustManager();
            SSLContext context = SSLContext.getInstance("TLS");
            context.init(null, new MyX509TrustManager[] { myX509TrustManager}, null);
            return context;
        }

        catch(Exception e) {
            Log.error(Log.Context.Net, e);
            return null;
        }
    }

    private SSLContext getSSLContext() {
        if(this.sslContext == null) {
            this.sslContext = createMySSLContext();
        }

        return this.sslContext;
    }

    public Socket createSocket(String host, int port, InetAddress clientHost, int clientPort) throws IOException {
        return getSSLContext().getSocketFactory().createSocket(host, port, clientHost, clientPort);
    }

    public Socket createSocket(final String host, final int port, final InetAddress localAddress, final int localPort, final HttpConnectionParams params) throws IOException {
        if(params == null) {
            throw new IllegalArgumentException("Parameters may not be null");
        }

        int timeout = params.getConnectionTimeout();
        SocketFactory socketFactory = getSSLContext().getSocketFactory();

        if(timeout == 0) {
            return socketFactory.createSocket(host, port, localAddress, localPort);
        }

        else {
            Socket socket = socketFactory.createSocket();
            SocketAddress localAddr = new InetSocketAddress(localAddress, localPort);
            SocketAddress remoteAddr = new InetSocketAddress(host, port);
            socket.bind(localAddr);
            socket.connect(remoteAddr, timeout);
            return socket;
        }
    }

    public Socket createSocket(String host, int port) throws IOException {
        return getSSLContext().getSocketFactory().createSocket(host, port);
    }

    public Socket createSocket(Socket socket, String host, int port, boolean autoClose) throws IOException {
        return getSSLContext().getSocketFactory().createSocket(socket, host, port, autoClose);
    }

    public boolean equals(Object obj) {
        return ((obj != null) && obj.getClass().equals(MySSLProtocolSocketFactory.class));
    }

    public int hashCode() {
        return MySSLProtocolSocketFactory.class.hashCode();
    }
}
