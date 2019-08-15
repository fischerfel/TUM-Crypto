public class TLSSocketFactory extends SSLSocketFactory {

private SSLSocketFactory delegate;

public TLSSocketFactory() throws KeyManagementException, NoSuchAlgorithmException {
    SSLContext context = SSLContext.getInstance("TLS");
    context.init(null, null, null);
    delegate = context.getSocketFactory();
}

@Override
public String[] getDefaultCipherSuites() {
    return new String[]{"TLSv1.2", "TLS_RSA_WITH_AES_128_CBC_SHA"};
}

@Override
public String[] getSupportedCipherSuites() {
    return new String[]{"TLSv1.2", "TLS_RSA_WITH_AES_128_CBC_SHA"};
}

@Override
public Socket createSocket(Socket s, String host, int port, boolean autoClose) throws IOException {
    return enableTLSOnSocket(delegate.createSocket(s, host, port, autoClose));
}

@Override
public Socket createSocket(String host, int port) throws IOException, UnknownHostException {
    return enableTLSOnSocket(delegate.createSocket(host, port));
}

@Override
public Socket createSocket(String host, int port, InetAddress localHost, int localPort) throws IOException, UnknownHostException {
    return enableTLSOnSocket(delegate.createSocket(host, port, localHost, localPort));
}

@Override
public Socket createSocket(InetAddress host, int port) throws IOException {
    return enableTLSOnSocket(delegate.createSocket(host, port));
}

@Override
public Socket createSocket(InetAddress address, int port, InetAddress localAddress, int localPort) throws IOException {
    return enableTLSOnSocket(delegate.createSocket(address, port, localAddress, localPort));
}

private Socket enableTLSOnSocket(Socket socket) {
    if (socket != null && (socket instanceof SSLSocket)) {
        ((SSLSocket) socket).setEnabledProtocols(new String[]{"TLSv1.2"});
    }
    return socket;
}
