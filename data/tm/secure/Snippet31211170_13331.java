public class PubKeyManager implements X509TrustManager {

    public Context mContext;

    @Override
    public void checkClientTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {}

    @Override
    public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {

        if (chain == null) {
            throw new IllegalArgumentException("checkServerTrusted: X509Certificate array is null");
        }

        if (!(chain.length > 0)) {
            throw new IllegalArgumentException("checkServerTrusted: X509Certificate is empty");
        }

        if (!(null != authType && authType.equalsIgnoreCase("RSA"))) {
            throw new CertificateException("checkServerTrusted: AuthType is not RSA");
        }

        // Perform customary SSL/TLS checks
        // get request cert
        try {
            TrustManagerFactory tmf = TrustManagerFactory.getInstance("X509");
            tmf.init((KeyStore) null);

            for (TrustManager trustManager : tmf.getTrustManagers()) {
                ((X509TrustManager) trustManager).checkServerTrusted(chain, authType);
            }
        } catch (Exception e) {
            throw new CertificateException(e);
        }

        //get stored certificate

        expected = //compare both certs

        if (!expected) {
            throw new CertificateException("checkServerTrusted");
        }
    }

    @Override
    public X509Certificate[] getAcceptedIssuers() {
        return new X509Certificate[0];
    }

    public void setContext(Context context) {
        mContext = context;
    }
}
