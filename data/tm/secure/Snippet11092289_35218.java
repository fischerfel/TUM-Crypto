public class ValicertX509TrustManager implements X509TrustManager {

    X509TrustManager pkixTrustManager;

    ValicertX509TrustManager() throws Exception {

        String valicertFile = "/certificates/ValicertRSAPublicRootCAv1.cer";
        String commwebDRFile = "/certificates/DR_10570.migs.mastercard.com.au.crt";
        String commwebPRODFile = "/certificates/PROD_10549.migs.mastercard.com.au.new.crt";

        Certificate valicert = CertificateFactory.getInstance("X509").generateCertificate(this.getClass().getResourceAsStream(valicertFile));
        Certificate commwebDR = CertificateFactory.getInstance("X509").generateCertificate(this.getClass().getResourceAsStream(commwebDRFile));
        Certificate commwebPROD = CertificateFactory.getInstance("X509").generateCertificate(this.getClass().getResourceAsStream(commwebPRODFile));

        KeyStore keyStore = KeyStore.getInstance("JKS");
        keyStore.load(null, "".toCharArray());
        keyStore.setCertificateEntry("valicert", valicert);
        keyStore.setCertificateEntry("commwebDR", commwebDR);
        keyStore.setCertificateEntry("commwebPROD", commwebPROD);

        TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance("PKIX");
        trustManagerFactory.init(keyStore);

        TrustManager trustManagers[] = trustManagerFactory.getTrustManagers();

        for(TrustManager trustManager : trustManagers) {
            if(trustManager instanceof X509TrustManager) {
                pkixTrustManager = (X509TrustManager) trustManager;
                return;
            }
        }

        throw new Exception("Couldn't initialize");
    }

    public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
        pkixTrustManager.checkServerTrusted(chain, authType);
    }

    public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
        pkixTrustManager.checkServerTrusted(chain, authType);
    }

    public X509Certificate[] getAcceptedIssuers() {
        return pkixTrustManager.getAcceptedIssuers();
    }
}
