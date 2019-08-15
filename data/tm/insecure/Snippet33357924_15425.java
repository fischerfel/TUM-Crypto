//Main:
SSLHandler handler = new SSLHandler();
handler.createSecureSocket("localhost", 7431);

byte[] data = {1,4,1,1,1,1};
handler.getOutputStream().write(data);
handler.getOutputStream().write(data);

// SSLHandler
public class SSLHandler {

    // SSL Socket erstellen
    SSLSocket sslSocket;

    public void createSecureSocket(String ip, int port) throws UnknownHostException, IOException, KeyManagementException, NoSuchAlgorithmException {

        SSLSocketFactory factory = (SSLSocketFactory) new DefaultTrustManager().createSSLFactory("TLS");

        sslSocket = (SSLSocket) factory.createSocket(ip, port);
    }

    public OutputStream getOutputStream() throws IOException {
        return sslSocket.getOutputStream();
    }

    public InputStream getInputStream() throws IOException {
        return sslSocket.getInputStream();
    }

}

//Custom Trust Manager
public class DefaultTrustManager implements X509TrustManager {

    @Override
    public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {}

    @Override
    public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {}

    @Override
    public X509Certificate[] getAcceptedIssuers() {
        return null;
    }

    public SSLSocketFactory createSSLFactory(String protocol) throws NoSuchAlgorithmException, KeyManagementException {
        SSLContext sslContext = SSLContext.getInstance(protocol);

        TrustManager[] byPassTrustManager = new TrustManager[] {this};

        sslContext.init(null, byPassTrustManager, new SecureRandom());

        return sslContext.getSocketFactory();
    }
}
