public class MySSLSocketFactory extends SSLSocketFactory {
SSLContext sslContext = SSLContext.getInstance("SSLv3");

public MySSLSocketFactory(KeyStore truststore) throws NoSuchAlgorithmException, KeyManagementException, KeyStoreException, UnrecoverableKeyException {
    super(truststore);

    TrustManager tm = new X509TrustManager() {
        public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {}
        public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {}
        public X509Certificate[] getAcceptedIssuers() {
            return null;
        }
    };

    sslContext.init(null, new TrustManager[] { tm }, null);
}

@Override
public Socket createSocket(Socket socket, String host, int port, boolean autoClose) throws IOException, UnknownHostException {
    SSLSocket S = (SSLSocket) sslContext.getSocketFactory().createSocket(socket, host, port, autoClose);
    S.setEnabledProtocols(new String[] {"SSLv3"});
    return S;
}

@Override
public Socket createSocket() throws IOException {
    SSLSocket S = (SSLSocket) sslContext.getSocketFactory().createSocket();
    S.setEnabledProtocols(new String[] {"SSLv3"});
    return S;
}
