public class MyTrustManager implements X509TrustManager {
MyTrustManager() {
    // create/load keystore
}

public void checkClientTrusted(X509Certificate chain[], String authType)
        throws CertificateException {
}

public void checkServerTrusted(X509Certificate chain[], String authType)
        throws CertificateException {
}

public X509Certificate[] getAcceptedIssuers() {
    return null;
}
