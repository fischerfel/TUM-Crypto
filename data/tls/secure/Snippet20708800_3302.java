public class MySSLSocketFactory extends SSLSocketFactory
                                throws NoSuchAlgorithmException {

    private SSLContext mSSLContext;

    public MySSLSocketFactory(KeyManager km) {
        ...
        mSSLContext = SSLContext.getInstance("TLSv1.2");
        ...
        mSSLContext.init(new KeyManager[] {km}, null, null);
        ...
    }

    @Override
    public Socket createSocket(Socket socket, String host, int port, boolean autoClose)
                    throws IOException {
        SSLSocket s = (SSLSocket)mSSLContext.getSocketFactory().createSocket(socket, host, port, autoClose);
        s.setEnabledProtocols(new String[] {"TLSv1.2"} );
        return s;
    }

    ...
}
