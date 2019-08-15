public class KeyPinStore {
private static Context context = null;
private static KeyPinStore instance = null;
private SSLContext sslContext = SSLContext.getInstance("SSLv3");
//private SSLContext sslContext = SSLContext.getInstance("TLS");
public static synchronized KeyPinStore getInstance(Context mContext) throws CertificateException, IOException, KeyStoreException,
        NoSuchAlgorithmException, KeyManagementException {
    if (instance == null) {
        context = mContext;
        instance = new KeyPinStore();
    }
    return instance;
}

private KeyPinStore() throws CertificateException, IOException, KeyStoreException, NoSuchAlgorithmException, KeyManagementException {
    // Load CAs from an InputStream
    // (could be from a resource or ByteArrayInputStream or ...)
    CertificateFactory cf = CertificateFactory.getInstance("X.509");
    // randomCA.crt should be in the Assets directory
    InputStream caInput = new BufferedInputStream(context.getAssets().open("yourCertificate.crt"));


    Certificate ca;
    try {
        ca = cf.generateCertificate(caInput);
        System.out.println("ca=" + ((X509Certificate) ca).getSubjectDN());
    } finally {
        caInput.close();
    }

    // Create a KeyStore containing our trusted CAs
    String keyStoreType = KeyStore.getDefaultType();
    KeyStore keyStore = KeyStore.getInstance(keyStoreType);
    keyStore.load(null, null);
    keyStore.setCertificateEntry("ca", ca);

    // Create a TrustManager that trusts the CAs in our KeyStore
    String tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
    TrustManagerFactory tmf = TrustManagerFactory.getInstance(tmfAlgorithm);
    tmf.init(keyStore);

    // Create an SSLContext that uses our TrustManager
    // SSLContext context = SSLContext.getInstance("TLS");
    sslContext.init(null, tmf.getTrustManagers(), null);
}

public SSLContext getContext() {
    return sslContext;
}
