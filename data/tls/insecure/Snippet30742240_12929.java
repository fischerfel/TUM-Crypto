public class MySSLSocketFactory extends SSLSocketFactory {
        SSLContext sslContext = SSLContext.getInstance("TLS");

        ...

        @Override
        public Socket createSocket(Socket socket, String host, int port, boolean autoClose) throws IOException, UnknownHostException {
            final SSLSocket sslSocket = (SSLSocket) sslContext.getSocketFactory().createSocket(socket, host, port, autoClose);
            sslSocket.setEnabledCipherSuites(sslSocket.getSupportedCipherSuites());
            return sslSocket;
        }

        @Override
        public Socket createSocket() throws IOException {
            final SSLSocket sslSocket = (SSLSocket) sslContext.getSocketFactory().createSocket();
            sslSocket.setEnabledCipherSuites(sslSocket.getSupportedCipherSuites());
            return sslSocket;
        }
}
