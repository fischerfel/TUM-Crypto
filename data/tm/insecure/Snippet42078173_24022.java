private Socket doSSLHandshake(Socket socket, String host, int port) throws IOException {
    TrustManager[] trustAllCerts = new TrustManager[]{
            new X509TrustManager(){
                public X509Certificate[] getAcceptedIssuers(){ return null; }
                public void checkClientTrusted(X509Certificate[] certs, String authType) {}
                public void checkServerTrusted(X509Certificate[] certs, String authType) {}
            }
    };

    try {
        SSLContext sslContext = SSLContext.getInstance("SSL");
        sslContext.init(null, trustAllCerts, new SecureRandom());
        SSLSocket sslSocket = (SSLSocket) sslContext.getSocketFactory().createSocket(socket, host, port, true);
        sslSocket.setEnabledProtocols(sslSocket.getSupportedProtocols());
        sslSocket.setEnableSessionCreation(true);
        sslSocket.startHandshake();
        return sslSocket;
    } catch (KeyManagementException | NoSuchAlgorithmException e) {
        throw new IOException("Could not do handshake: " + e);
    }
}
