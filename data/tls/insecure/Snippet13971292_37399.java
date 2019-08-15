private void setSSLConnection(WSBindingProvider bp){
    KeyStore ks = KeyStore.getInstance("pkcs12");
    ks.load(this.getClass().getClassLoader().getResourceAsStream("WebServices.pfx"), "password".toCharArray());

    KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
    kmf.init(ks, "password".toCharArray());

    SSLContext sslContext = SSLContext.getInstance("TLS");
    KeyStore ts = KeyStore.getInstance("JKS");
    ts.load(this.getClass().getClassLoader().getResourceAsStream("trustStore"), "pass".toCharArray());

    TrustManagerFactory tmf = TrustManagerFactory.getInstance("SunX509");
    tmf.init(ts);
    sslContext.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);

    bp.getRequestContext().put(JAXWSProperties.SSL_SOCKET_FACTORY, sslContext.getSocketFactory());

}
