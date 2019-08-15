public class TestTrustManager implements X509TrustManager {
    @Override
    public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {

       for (int i = 0; i < chain.length; ++i) {
        System.out.println(chain[i]);
       }

       decorated.checkServerTrusted(chain, authType);
  }
}
