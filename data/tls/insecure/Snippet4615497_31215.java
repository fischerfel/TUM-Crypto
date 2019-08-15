public class MySSLSocketFactory extends SocketFactory {
    private SSLSocketFactory sf;

    public MySSLSocketFactory() {
        KeyStore keyStore = ... /* Get a keystore containing the self-signed certificate) */
        TrustManagerFactory tmf = TrustManagerFactory.getInstance();
        tmf.init(keyStore);
        SSLContext ctx = SSLContext.getInstance("TLS");
        ctx.init(null, tmf.getTrustManagers(), null);
        sf = ctx.getSocketFactory();
    }

    /* delegate SSLSocketFactory public methods to sf */
    ...
}
