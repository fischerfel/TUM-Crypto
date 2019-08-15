public class MySSLTrust {
public static OkHttpClient trustcert(Context context){
    OkHttpClient okHttpClient = new OkHttpClient();
    try {
        KeyStore ksTrust = KeyStore.getInstance("BKS");
        InputStream instream = context.getResources().openRawResource(R.raw.my_keystore);
        ksTrust.load(instream, "secret".toCharArray());
        // TrustManager decides which certificate authorities to use.
        TrustManagerFactory tmf = TrustManagerFactory
                .getInstance(TrustManagerFactory.getDefaultAlgorithm());
        tmf.init(ksTrust);
        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(null, tmf.getTrustManagers(), null);
        okHttpClient.setSslSocketFactory(sslContext.getSocketFactory());
    } catch (KeyStoreException | IOException | NoSuchAlgorithmException | CertificateException | KeyManagementException e) {
        e.printStackTrace();
    }
    return okHttpClient;
}
}
