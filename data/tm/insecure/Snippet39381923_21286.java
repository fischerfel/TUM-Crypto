public class CertificationUtils {

public static X509TrustManager x509TrustManager;

public static SSLContext sslContext = null;

public static HostnameVerifier DO_NOT_VERIFY;

public static void init(){

    x509TrustManager = new X509TrustManager() {
        @Override
        public void checkClientTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {

        }

        @Override
        public void checkServerTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {

        }

        @Override
        public X509Certificate[] getAcceptedIssuers() {
            X509Certificate[] x509Certificates = new X509Certificate[0];
            return x509Certificates;
        }

    };

    try {
        sslContext = SSLContext.getInstance("SSL");

        sslContext.init(null, new TrustManager[]{x509TrustManager}, new SecureRandom());

    } catch (NoSuchAlgorithmException e) {
        e.printStackTrace();
    } catch (KeyManagementException e) {
        e.printStackTrace();
    }

    DO_NOT_VERIFY = new HostnameVerifier() {
        @Override
        public boolean verify(String hostname, SSLSession session) {
            return true;
        }
    };

}
