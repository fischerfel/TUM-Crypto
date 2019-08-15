    public SSLSocketFactory getSSLSocketFactory (InputStream keyStore, String password) throws MqttSecurityException {
    try{

        SSLContext ctx = null;
        SSLSocketFactory sslSockFactory=null;

        KeyStore ks;
        ks = KeyStore.getInstance("PKCS12");
        ks.load(keyStore, password.toCharArray());

        TrustManagerFactory tmf = TrustManagerFactory.getInstance("X509");
        tmf.init(ks);
        TrustManager[] tm = tmf.getTrustManagers();
        ctx = SSLContext.getInstance("TLS");
        ctx.init(null, tm, null);
        sslSockFactory = ctx.getSocketFactory();
        return sslSockFactory;

    } catch (KeyStoreException | CertificateException | IOException | NoSuchAlgorithmException | KeyManagementException e) {
        throw new MqttSecurityException(e);
    }
}
