public class MySocketFactory implements SocketFactory, LayeredSocketFactory {
private SSLContext mSslContext = null;

{
    try {
        mSslContext = SSLContext.getInstance("TLS");

        TrustManager trustManager = new X509TrustManager() {

            @Override
            public X509Certificate[] getAcceptedIssuers() {
                return null;
            }

            @Override
            public void checkServerTrusted(X509Certificate[] chain, String authType)
                    throws CertificateException {
                // !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
                // NOTE : This is where we can calculate the certificate's fingerprint,
                // show it to the user and throw an exception in case he doesn't like it
            }

            @Override
            public void checkClientTrusted(X509Certificate[] chain, String authType)
                    throws CertificateException {
            }
        };

        mSslContext.init(null, new TrustManager[]{ trustManager }, new SecureRandom());
    } catch (Exception e) {
        e.printStackTrace();
    }
}

@Override
public Socket connectSocket(Socket socket, String host, int port,
        InetAddress localAddress, int localPort, HttpParams params)
                throws IOException, UnknownHostException, ConnectTimeoutException {

    int connTimeout = HttpConnectionParams.getConnectionTimeout(params);
    int soTimeout = HttpConnectionParams.getSoTimeout(params);

    SocketAddress socketAddress = new InetSocketAddress(localAddress, localPort);
    socket.bind(socketAddress);
    socket.connect(new InetSocketAddress(host, port), connTimeout);
    socket.setSoTimeout(soTimeout);

    return socket;
}

@Override
public Socket createSocket() throws IOException {
    return mSslContext.getSocketFactory().createSocket();
}

@Override
public boolean isSecure(Socket sock) throws IllegalArgumentException {
    return true;
}



@Override
public Socket createSocket(Socket socket, String host, int port,
        boolean autoClose) throws IOException, UnknownHostException {

    return mSslContext.getSocketFactory().createSocket(socket, host, port, autoClose);
}
