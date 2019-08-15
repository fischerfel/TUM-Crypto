public class MySSLSocketFactory extends SSLSocketFactory {
SSLContext sslContext = SSLContext.getInstance("TLS");

public MySSLSocketFactory(KeyStore ctx) throws NoSuchAlgorithmException, KeyManagementException, KeyStoreException, UnrecoverableKeyException {
    super(ctx);

    TrustManager tm = new X509TrustManager() {
        public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
        }

        public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
        }

        public X509Certificate[] getAcceptedIssuers() {
            return null;
        }
    };

    sslContext.init(null, new TrustManager[]{tm}, null);
}

@Override
public Socket createSocket(Socket socket, String host, int port, boolean autoClose) throws IOException, UnknownHostException {
    return sslContext.getSocketFactory().createSocket(socket, host, port, autoClose);
}

@Override
public Socket createSocket() throws IOException {
    return sslContext.getSocketFactory().createSocket();
}
