public class MyX509TrustManager implements X509TrustManager {

    X509TrustManager pkixTrustManager;

    MyX509TrustManager() throws Exception {

        String certFile = "/certificates/MyCertFile.cer";

        Certificate myCert = CertificateFactory.getInstance("X509").generateCertificate(this.getClass().getResourceAsStream(valicertFile));

        KeyStore keyStore = KeyStore.getInstance("JKS");
        keyStore.load(null, "".toCharArray());
        keyStore.setCertificateEntry("myCert", myCert);

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
