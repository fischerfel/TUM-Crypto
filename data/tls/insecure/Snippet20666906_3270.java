private Socket createSSLSocket(InetAddress address, int port) {
    Socket socket;

    try {
        InputStream trustStream = new FileInputStream(new File("truststore"));
        KeyStore trustStore = KeyStore.getInstance("JKS");

                    // load the stream to your store
        trustStore.load(trustStream, trustPassword);

        // initialize a trust manager factory with the trusted store
        TrustManagerFactory trustFactory = TrustManagerFactory.getInstance("PKIX", "SunJSSE");                  trustFactory.init(trustStore);

        // get the trust managers from the factory
        TrustManager[] trustManagers = trustFactory.getTrustManagers();

        // initialize an ssl context to use these managers and set as default
        SSLContext sslContext = SSLContext.getInstance("SSL");
        sslContext.init(null, trustManagers, null);
        if(address == null) {
            socket = sslContext.getSocketFactory().createSocket();
        } else {
            socket = sslContext.getSocketFactory().createSocket(address, port);
        }
    } catch (Exception e) {
        System.out.println(e.getLocalizedMessage());
        return null;
    }
    return socket;
}
