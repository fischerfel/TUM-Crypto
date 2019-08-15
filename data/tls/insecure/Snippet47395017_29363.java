public class DummySSLSocketFactory extends SSLSocketFactory {

    private static final Logger LOG = LoggerFactory.getLogger(DummySSLSocketFactory.class);
    private SSLSocketFactory factory;

    public DummySSLSocketFactory() {
        try {
            SSLContext sslContext = SSLContext.getInstance("TLS");
            TrustManager[] trustManagers = new TrustManager[] {new DummyTrustManager()};
            sslContext.init(null, trustManagers, new java.security.SecureRandom());
            factory = sslContext.getSocketFactory();
        } catch (Exception e) {
            throw new RuntimeCamelException("Error creating DummySSLSocketFactory: " + e.getMessage(), e);
        }
    }

    /**
     * Must provide this getDefault operation for JavaMail to be able to use this factory.
     */
    public static SocketFactory getDefault() {
        LOG.warn("Camel is using DummySSLSocketFactory as SSLSocketFactory (only to be used for testing purpose)");
        return new DummySSLSocketFactory();
    }

    public String[] getDefaultCipherSuites() {
        return factory.getDefaultCipherSuites();
    }

    public String[] getSupportedCipherSuites() {
        return factory.getSupportedCipherSuites();
    }

    public Socket createSocket(Socket socket, String host, int port, boolean autoClose) throws IOException {
        return factory.createSocket(socket, host, port, autoClose);
    }

    public Socket createSocket(String host, int port) throws IOException {
        return factory.createSocket(host, port);
    }

    public Socket createSocket(String host, int port, InetAddress localAddress, int localPort)
        throws IOException {
        return factory.createSocket(host, port, localAddress, localPort);
    }

    public Socket createSocket(InetAddress host, int port) throws IOException {
        return factory.createSocket(host, port);
    }

    public Socket createSocket(InetAddress address, int port, InetAddress localAddress, int localPort)
        throws IOException {
        return factory.createSocket(address, port, localAddress, localPort);
    }

    public Socket createSocket() throws IOException {
        // must have this createSocket method
        return factory.createSocket();
    }

}
