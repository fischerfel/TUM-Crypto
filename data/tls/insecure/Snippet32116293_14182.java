public class ConnectionPreparer {
    private static SSLSocketFactory sslSocketFactory;
    private Context context;
    public ConnectionPreparer (Context context){
        this.context = context;
    }
    private SSLSocketFactory getSSLSocketFactory(){
        if(sslSocketFactory==null){
            CertificateFactory cf = CertificateFactory.getInstance("X.509");
            InputStream caInput = new BufferedInputStream(context.getAssets().open("my_certificate.crt"));
            Certificate ca = cf.generateCertificate(caInput);

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
            SSLContext context = SSLContext.getInstance("TLS");
            context.init(null, tmf.getTrustManagers(), null);

            sslSocketFactory = context.getSocketFactory();
        }
        return sslSocketFactory;
    }
    public HttpsURLConnection prepareConnection(){
        URL url = SOME_URL;            
        HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
        connection.setSSLSocketFactory(getSSLSocketFactory());
        // and so on
    }
}
