class TrustAllSSLSocketFactory implements ProtocolSocketFactory {

    public static final TrustManager[] TRUST_ALL_CERTS = new TrustManager[]{
        new X509TrustManager() {
            public void checkClientTrusted(final X509Certificate[] certs, final String authType) {

            }

            public void checkServerTrusted(final X509Certificate[] certs, final String authType) {

            }

            public X509Certificate[] getAcceptedIssuers() {
                return null;
            }
        }
    };

    private TrustManager[] getTrustManager() {
        return TRUST_ALL_CERTS;
    }

    public Socket createSocket(final String host, final int port, final InetAddress clientHost,
                               final int clientPort) throws IOException {
        return getSocketFactory().createSocket(host, port, clientHost, clientPort);
    }

    @Override
    public Socket createSocket(final String host, final int port, final InetAddress localAddress,
                               final int localPort, final HttpConnectionParams params) throws IOException {
        return createSocket(host, port);
    }

    public Socket createSocket(final String host, final int port) throws IOException {
        return getSocketFactory().createSocket(host, port);
    }

    private SocketFactory getSocketFactory() throws UnknownHostException {
        TrustManager[] trustAllCerts = getTrustManager();

        try {
            SSLContext context = SSLContext.getInstance("SSL");
            context.init(null, trustAllCerts, new SecureRandom());

            final SSLSocketFactory socketFactory = context.getSocketFactory();
            HttpsURLConnection.setDefaultSSLSocketFactory(socketFactory);
            return socketFactory;
        } catch (NoSuchAlgorithmException | KeyManagementException exception) {
            throw new UnknownHostException(exception.getMessage());
        }
    }
}
