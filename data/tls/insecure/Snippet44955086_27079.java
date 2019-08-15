public class SingletonSSLHandler() {
    private SSLSocket sslContext;
    private SSLSocketFactory sslSocketFactory;

    //GetInstance() and etc.

    private SingletonSSLHandler() {
        sslContext = SSLContext.getInstance("TLS");
        TrustManager[] trustManager = new TrustManager[]{
            new MyOwnTrustManager()
        };

        sslContext.init(null, trustManager, null);
        sslSocketFactory = sslContext.getSocketFactory();
    }

    public static Socket upgradeToSSL(Socket plainSocket) {

        sslSocket = (SSLSocket) sslsocketfactory.createSocket(
                    remoteSocket,
                    remoteSocket.getInetAddress().getHostAddress(),
                    remoteSocket.getPort(),
                    true);

        return sslSocket;
    }
}
