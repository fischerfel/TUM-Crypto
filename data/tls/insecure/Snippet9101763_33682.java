public class SelfCertificatesSocketFactory extends SSLSocketFactory {

SSLContext sslContext = SSLContext.getInstance("TLS");

public SelfCertificatesSocketFactory(KeyStore trustStore) throws NoSuchAlgorithmException,UnrecoverableKeyException,KeyStoreException,KeyManagementException {
    super(trustStore);

      TrustManager tm = new X509TrustManager() {
            public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
            }

            public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
            }

            public X509Certificate[] getAcceptedIssuers() {
                return null;
            }
        };




}

@Override
public Socket createSocket() throws IOException {
    return sslContext.getSocketFactory().createSocket();
}



@Override
public Socket createSocket(Socket socket, String host, int port,
        boolean autoClose) throws IOException, UnknownHostException {
    return sslContext.getSocketFactory().createSocket(socket,host,port,autoClose);
}



}
