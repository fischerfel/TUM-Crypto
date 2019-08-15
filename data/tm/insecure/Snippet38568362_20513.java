public class KeyUnPinStore {

private static Context context = null;
private static KeyUnPinStore instance = null;
private SSLContext sslContext = SSLContext.getInstance("TLS");

public static synchronized KeyUnPinStore getInstance(Context mContext) throws CertificateException, IOException, KeyStoreException,
        NoSuchAlgorithmException, KeyManagementException {
    if (instance == null) {
        context = mContext;
        instance = new KeyUnPinStore();
    }
    return instance;
}

private KeyUnPinStore() throws CertificateException, IOException, KeyStoreException, NoSuchAlgorithmException, KeyManagementException {
    // Create a trust manager that does not validate certificate chains
    TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
        public X509Certificate[] getAcceptedIssuers() {
            return null;
        }

        public void checkClientTrusted(X509Certificate[] certs, String authType) {
        }

        public void checkServerTrusted(X509Certificate[] certs, String authType) {
        }
    } };

    // Create all-trusting host name verifier
    // Create an SSLContext that uses our TrustManager
    // SSLContext context = SSLContext.getInstance("TLS");
    sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
}

public SSLContext getContext() {
    return sslContext;
}
