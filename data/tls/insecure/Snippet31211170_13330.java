public class PinningSSLSocketFactory extends SSLSocketFactory {
    SSLContext sslContext = SSLContext.getInstance("TLS");

    PubKeyManager pkm;

    public PinningSSLSocketFactory(KeyStore truststore) throws NoSuchAlgorithmException, KeyManagementException,
        KeyStoreException, UnrecoverableKeyException {
        super(truststore);

        pkm  = new PubKeyManager();

        TrustManager tm[] = { pkm };

        sslContext.init(null, tm, null);

    }

    @Override
    public Socket createSocket(Socket socket, String host, int port, boolean autoClose) throws IOException,
        UnknownHostException {
        return sslContext.getSocketFactory().createSocket(socket, host, port, autoClose);
    }

    @Override
    public Socket createSocket() throws IOException {
        return sslContext.getSocketFactory().createSocket();
    }

    public void setContext(Context context) {
        pkm.setContext(context);
    }
} 
