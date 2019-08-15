try{
    TrustManager[] trustManager = new TrustManager[] { new X509TrustManager() {
        public X509Certificate[] getAcceptedIssuers() { return null; }
        public void checkClientTrusted(X509Certificate[] certs, String authType) {}
        public void checkServerTrusted(X509Certificate[] certs, String authType) {}
    } };

    SSLContext sslContext = null;

    sslContext = SSLContext.getInstance("TLS");
    sslContext.init(null, trustManager, new SecureRandom());
    SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();
    FTPClient client = new FTPClient();

    client.setSSLSocketFactory(sslSocketFactory);
    client.setSecurity(FTPClient.SECURITY_FTPES);

    client.connect("xxx.xxx.xxx", 21);
    System.out.println("Connected (" + client.isConnected() + ") to host " + client.getHost() + ":" + client.getPort());
    client.login("xxxxx", "xxxxx");            
}catch (Exception e) {
    e.printStackTrace();
}
