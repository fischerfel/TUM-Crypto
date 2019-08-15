private static SSLContext createSSLContext(String cert, char[] pass) throws Exception {
    //Load KeyStore in JKS format:
    KeyStore keyStore = KeyStore.getInstance("jks");
    keyStore.load(new FileInputStream(cert), "password".toCharArray());

    //Create key manager:
    KeyManagerFactory kmFactory = KeyManagerFactory.getInstance("SunX509");
    kmFactory.init(keyStore, pass); KeyManager[] km = kmFactory.getKeyManagers();

    //Create trust manager:
    TrustManagerFactory tmFactory = TrustManagerFactory.getInstance("SunX509");
    tmFactory.init(keyStore); TrustManager[] tm = tmFactory.getTrustManagers();

    //Create SSLContext with protocol:
    SSLContext ctx = SSLContext.getInstance("TLSv1.2");
    ctx.init(km, tm, null); return ctx;
}
