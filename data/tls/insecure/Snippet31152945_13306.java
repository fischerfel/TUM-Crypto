public class CustomSSLSocketFactory extends SSLSocketFactory {

    SSLContext sslContext = SSLContext.getInstance("TLS");

    public CustomSSLSocketFactory(KeyStore truststore)
            throws NoSuchAlgorithmException, KeyManagementException,
            KeyStoreException, UnrecoverableKeyException {
        super(truststore);

        TrustManager tm = new CustomX509TrustManager();

        sslContext.init(null, new TrustManager[] { tm }, null);
    }

    public CustomSSLSocketFactory(SSLContext context)
            throws KeyManagementException, NoSuchAlgorithmException,
            KeyStoreException, UnrecoverableKeyException {
        super(null);
        sslContext = context;
    }

    @Override
    public Socket createSocket(Socket socket, String host, int port,
                               boolean autoClose) throws IOException, UnknownHostException {
        Socket newSocket =  sslContext.getSocketFactory().createSocket(socket, host, port,
                autoClose);
        ((SSLSocket) newSocket).setEnabledCipherSuites(((SSLSocket) newSocket).getSupportedCipherSuites());
        AdjustSocket(newSocket);
        return newSocket;
    }

    @Override
    public Socket createSocket() throws IOException {
        Socket socket = sslContext.getSocketFactory().createSocket();
        ((SSLSocket) socket).setEnabledCipherSuites(((SSLSocket) socket).getSupportedCipherSuites());
        adjustSocket(socket);
        return socket;
    }

    private void adjustSocket(Socket socket)
    {
        String[] cipherSuites = ((SSLSocket) socket).getSSLParameters().getCipherSuites();
        ArrayList<String> cipherSuiteList = new ArrayList<String>(Arrays.asList(cipherSuites));

        cipherSuiteList.add("TLS_RSA_WITH_3DES_EDE_CBC_SHA");
        cipherSuites = cipherSuiteList.toArray(new String[cipherSuiteList.size()]);
        ((SSLSocket) socket).getSSLParameters().setCipherSuites(cipherSuites);

        String[] protocols = ((SSLSocket) socket).getSSLParameters().getProtocols();
        ArrayList<String> protocolList = new ArrayList<String>(Arrays.asList(protocols));

        for (int ii = protocolList.size() - 1; ii >= 0; --ii )
        {
            if ((protocolList.get(ii).contains("SSLv3")) || (protocolList.get(ii).contains("TLSv1.1")) || (protocolList.get(ii).contains("TLSv1.2")))
                protocolList.remove(ii);
        }

        protocols = protocolList.toArray(new String[protocolList.size()]);
        ((SSLSocket)socket).setEnabledProtocols(protocols);
    }
}
