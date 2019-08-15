public class LDAPSSocketFactory extends SSLSocketFactory {

private SSLSocketFactory actualSocketFactory;

public LDAPSSocketFactory() {

    InputStream certificateInputStream = this.getClass().getClassLoader().getResourceAsStream("yourcert.pfx");

    try {

        KeyStore pkcs12 = KeyStore.getInstance("pkcs12");

        pkcs12.load(certificateInputStream, "".toCharArray());

        TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());

        tmf.init(pkcs12);

        SSLContext ctx = SSLContext.getInstance("TLS");

        ctx.init(null, tmf.getTrustManagers(), null);

        actualSocketFactory = ctx.getSocketFactory();

    } catch (KeyStoreException e) {
        e.printStackTrace();
    } catch (CertificateException e) {
        e.printStackTrace();
    } catch (NoSuchAlgorithmException e) {
        e.printStackTrace();
    } catch (IOException e) {
        e.printStackTrace();
    } catch (KeyManagementException e) {
        e.printStackTrace();
    }

}

//Override methods by simply deligating them to the actualSocketFactory

}
