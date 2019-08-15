private SSLSocketFactory getSSLFactory() throws Exception {

    KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
    InputStream is = this.getClass().getResourceAsStream("license.store");

    if(is ==null) {
        return null;
    }

    keyStore.load(is, "asdf1234".toCharArray());
    is.close();

    TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
    tmf.init(keyStore);

    SSLContext ctx = SSLContext.getInstance("TLS");
    ctx.init(null, tmf.getTrustManagers(), null);

    return ctx.getSocketFactory();
}
