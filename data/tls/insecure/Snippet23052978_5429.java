public class MySSLSocketFactory extends SSLSocketFactory {
private SSLContext sslContext;

public MySSLSocketFactory(Context context)
        throws NoSuchAlgorithmException, KeyManagementException,
        KeyStoreException, UnrecoverableKeyException, CertificateException,
        IOException {
    super(null);
    sslContext = SSLContext.getInstance("TLS");
    sslContext.init(null, new TrustManager[] {
        new MyTrustManager(context)
    }, null);
}

@Override
public Socket createSocket() throws IOException {
    // TODO Auto-generated method stub
    return sslContext.getSocketFactory().createSocket();
}

@Override
public Socket createSocket(Socket socket, String host, int port,
        boolean autoClose) throws IOException, UnknownHostException {
    // TODO Auto-generated method stub
    return sslContext.getSocketFactory().createSocket(socket, host, port,
            autoClose);
}

}
