public class TrustSelectCertSSLSocketFactory extends SSLSocketFactory {
SSLContext sslContext = SSLContext.getInstance("TLS");
public TrustSelectCertSSLFactory(KeyStore truststore, Context context) throws NoSuchAlgorithmException, KeyManagementException, KeyStoreException,
        UnrecoverableKeyException, CertificateException {
    super(truststore);
    String tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
    TrustManagerFactory tmf = TrustManagerFactory.getInstance(tmfAlgorithm);
    tmf.init(truststore);
    sslContext.init(null, tmf.getTrustManagers(), null);
}

@Override
public Socket createSocket(Socket socket, String host, int port, boolean autoClose) throws IOException, UnknownHostException {
    return sslContext.getSocketFactory().createSocket(socket, host, port, autoClose);
}

@Override
public Socket createSocket() throws IOException {
    return sslContext.getSocketFactory().createSocket();
}

}
