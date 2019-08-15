public class TLSSocketFactoryCompat extends SSLSocketFactory {

private SSLSocketFactory internalSSLSocketFactory;

public TLSSocketFactoryCompat() throws KeyManagementException, NoSuchAlgorithmException {
    SSLContext context = SSLContext.getInstance("TLS");
    context.init(null, null, null);
    internalSSLSocketFactory = context.getSocketFactory();
}

public TLSSocketFactoryCompat(TrustManager[] tm) throws KeyManagementException, NoSuchAlgorithmException {
    SSLContext context = SSLContext.getInstance("TLS");
    context.init(null, tm, new java.security.SecureRandom());
    internalSSLSocketFactory = context.getSocketFactory();
}

@Override
public String[] getDefaultCipherSuites() {
    return internalSSLSocketFactory.getDefaultCipherSuites();
}

@Override
public String[] getSupportedCipherSuites() {
    return internalSSLSocketFactory.getSupportedCipherSuites();
}

@Override
public Socket createSocket() throws IOException {
    return enableTLSOnSocket(internalSSLSocketFactory.createSocket());
}

@Override
public Socket createSocket(Socket s, String host, int port, boolean autoClose) throws IOException {
    return enableTLSOnSocket(internalSSLSocketFactory.createSocket(s, host, port, autoClose));
}

@Override
public Socket createSocket(String host, int port) throws IOException, UnknownHostException {
    return enableTLSOnSocket(internalSSLSocketFactory.createSocket(host, port));
}

@Override
public Socket createSocket(String host, int port, InetAddress localHost, int localPort) throws IOException, UnknownHostException {
    return enableTLSOnSocket(internalSSLSocketFactory.createSocket(host, port, localHost, localPort));
}

@Override
public Socket createSocket(InetAddress host, int port) throws IOException {
    return enableTLSOnSocket(internalSSLSocketFactory.createSocket(host, port));
}

@Override
public Socket createSocket(InetAddress address, int port, InetAddress localAddress, int localPort) throws IOException {
    return enableTLSOnSocket(internalSSLSocketFactory.createSocket(address, port, localAddress, localPort));
}

private Socket enableTLSOnSocket(Socket socket) {
    if(socket != null && (socket instanceof SSLSocket)) {
        //Create list of supported protocols
        ArrayList<String> supportedProtocols = new ArrayList<>();
        for (String protocol : ((SSLSocket)socket).getEnabledProtocols()) {

            //Log.d("TLSSocketFactory", "Supported protocol:" + protocol);
            //Only add TLS protocols (don't want ot support older SSL versions)
            if (protocol.toUpperCase().contains("TLS")) {
                supportedProtocols.add(protocol);
            }
        }
        //Force add TLSv1.1 and 1.2 if not already added
        if (!supportedProtocols.contains("TLSv1.1")) {
            supportedProtocols.add("TLSv1.1");
        }
        if (!supportedProtocols.contains("TLSv1.2")) {
            supportedProtocols.add("TLSv1.2");
        }

        String[] protocolArray = supportedProtocols.toArray(new String[supportedProtocols.size()]);
        /*for (int i = 0; i < protocolArray.length; i++) {
            Log.d("TLSSocketFactory", "protocolArray[" + i + "]" + protocolArray[i]);
        }*/

        //enable protocols in our list
        ((SSLSocket)socket).setEnabledProtocols(protocolArray);
    }
    return socket;
}
