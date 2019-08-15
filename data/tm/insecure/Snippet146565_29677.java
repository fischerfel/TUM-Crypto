
public class dummyTrustManager implements X509TrustManager {

        public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
            //do nothing
        }

        public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
            // do nothing
        }

        public X509Certificate[] getAcceptedIssuers() {
            //just return an empty issuer
            return new X509Certificate[0];
        }
    }
