public class MySSLSocketFactory extends SSLSocketFactory {
           SSLContext sslContext = SSLContext.getInstance("TLS");


           public MySSLSocketFactory() throws UnrecoverableKeyException, NoSuchAlgorithmException, KeyStoreException, KeyManagementException {
                super(null, null, null, null, null, null);

                final TrustManagerFactory trustMgrFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
                trustMgrFactory.init((KeyStore) null);

                sslContext.init(null, trustMgrFactory.getTrustManagers(), new SecureRandom());
            }

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
