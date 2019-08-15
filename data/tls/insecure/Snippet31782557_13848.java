public ServiceConnectionSE(Certificate ca, String url) throws IOException
{

    // Create a KeyStore containing our trusted CAs
    String keyStoreType = KeyStore.getDefaultType();
    KeyStore keyStore;
    SSLContext context = null;
    try {
        keyStore = KeyStore.getInstance(keyStoreType);
        keyStore.load(null, null);
        keyStore.setCertificateEntry("ca", ca);
        // Create a TrustManager that trusts the CAs in our KeyStore
        String tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
        TrustManagerFactory tmf = TrustManagerFactory.getInstance(tmfAlgorithm);
        tmf.init(keyStore);

        // Create an SSLContext that uses our TrustManager
        context = SSLContext.getInstance("TLS");
        context.init(null, tmf.getTrustManagers(), null);


    } catch (KeyStoreException | NoSuchAlgorithmException | CertificateException | KeyManagementException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    }


    // Tell the URLConnection to use a SocketFactory from our SSLContext


    connection = (HttpsURLConnection) new URL(url).openConnection();
    connection.setSSLSocketFactory(context.getSocketFactory());

    connection.setUseCaches(false);
    connection.setDoOutput(true);
    connection.setDoInput(true);
}
