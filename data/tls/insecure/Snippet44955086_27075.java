public class SSLHandler() {
    public Socket upgradeToSSL(Socket plainSocket) {
        SSLSocket sslContext = SSLContext.getInstance("TLS");
        TrustManager[] trustManager = new TrustManager[]{
            new MyOwnTrustManager()
        };

        sslContext.init(null, trustManager, null);
        SSLSocketFactory sslsocketfactory = sslContext.getSocketFactory();

        sslSocket = (SSLSocket) sslsocketfactory.createSocket(
                    remoteSocket,
                    remoteSocket.getInetAddress().getHostAddress(),
                    remoteSocket.getPort(),
                    true);

        return sslSocket;
    }
}
