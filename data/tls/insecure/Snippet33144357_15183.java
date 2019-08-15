public class SpeedportSSLSocketFactory extends SSLSocketFactory {

private final static Logger logger = Logger.getLogger(SpeedportSSLSocketFactory.class);

/**
 * the order of ciphers in this list is important here e.g. TLS_DHE_* must not stay above TLS_RSA_*
 */
private static final String[] APPROVED_CIPHER_SUITES = new String[]{
        "TLS_ECDHE_ECDSA_WITH_AES_128_GCM_SHA256",
        "TLS_ECDHE_RSA_WITH_AES_128_GCM_SHA256",
        "TLS_ECDHE_ECDSA_WITH_AES_256_CBC_SHA",
        "TLS_ECDHE_ECDSA_WITH_AES_128_CBC_SHA",
        "TLS_ECDHE_RSA_WITH_AES_128_CBC_SHA",
        "TLS_ECDHE_RSA_WITH_AES_256_CBC_SHA",
        "TLS_RSA_WITH_AES_128_GCM_SHA256",
        "TLS_RSA_WITH_AES_128_CBC_SHA",
        "TLS_RSA_WITH_AES_256_CBC_SHA",
        "TLS_DHE_RSA_WITH_AES_128_CBC_SHA",
        "TLS_DHE_RSA_WITH_AES_256_CBC_SHA",
        "TLS_DHE_RSA_WITH_AES_128_GCM_SHA256",
};

private SSLSocketFactory factory;

public SpeedportSSLSocketFactory() {
    try {
        SSLContext sslcontext = SSLContext.getInstance("TLS");
        sslcontext.init(null, new TrustManager[]{
                // accepts certs with valid but expired key chain (incl. root cert)
                new ExpiredSpeedportTrustManager()}, new java.security.SecureRandom());
        factory = sslcontext.getSocketFactory();
    } catch (Exception ex) {
        logger.error("Cannot create SpeedportSSLSocketFactory", ex);
    }
}

// dirty
private void injectHostname(InetAddress address, String host) {
    try {
        Field field = InetAddress.class.getDeclaredField("hostName");
        field.setAccessible(true);
        field.set(address, host);
    } catch (Exception ignored) {
        logger.error("Cannot inject hostName");
    }
}

public static SocketFactory getDefault() {
    return new SpeedportSSLSocketFactory();
}

public Socket createSocket() throws IOException {
    return factory.createSocket();
}

public Socket createSocket(Socket socket, String host, int port, boolean autoClose) throws IOException {
    return factory.createSocket(socket, host, port, autoClose);
}

public Socket createSocket(InetAddress addr, int port, InetAddress localAddr, int localPort) throws IOException {
    return factory.createSocket(addr, port, localAddr, localPort);
}

public Socket createSocket(InetAddress inaddr, int i) throws IOException {
    return factory.createSocket(inaddr, i);
}

public Socket createSocket(String host, int port, InetAddress localAddr, int localPort) throws IOException {
    return factory.createSocket(host, port, localAddr, localPort);
}

public Socket createSocket(String host, int port) throws IOException {

    InetAddress addr = InetAddress.getByName(host);
    injectHostname(addr, host);

    Socket socket = factory.createSocket(addr, port);
    ((SSLSocket) socket).setEnabledCipherSuites(getSupportedCipherSuites());
    return socket;
}

@Override
public String[] getDefaultCipherSuites() {
    return APPROVED_CIPHER_SUITES;
}

@Override
public String[] getSupportedCipherSuites() {
    return APPROVED_CIPHER_SUITES;
}
