public class CustomX509TrustManager implements X509TrustManager{
private X509Certificate[] serverX509CertificateArray;
    @Override
    public void checkClientTrusted(X509Certificate[] xcs, String string) throws CertificateException {
    }

    @Override
    public void checkServerTrusted(X509Certificate[] xcs, String string) throws CertificateException {
            //TODO xcs array include: OID,...
        this.serverX509CertificateArray = xcs;
    }

    @Override
    public X509Certificate[] getAcceptedIssuers() {
        return this.serverX509CertificateArray;
    }
}
