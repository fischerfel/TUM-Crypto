@SuppressLint("SdCardPath")
public HttpsURLConnection setUpHttpsConnection(String urlString)
{
    try
    {
        CertificateFactory cf = CertificateFactory.getInstance("X.509","BC");

        AssetManager assManager = context.getAssets();
        InputStream caInput = assManager.open("testCert.pfx");

        KeyStore keyStore = KeyStore.getInstance("PKCS12");

        Certificate ca = cf.generateCertificate(caInput);

        System.out.println("ca=" + ((X509Certificate) ca).getSubjectDN());

        keyStore.load(null, null);
        keyStore.setCertificateEntry("ca", ca);

        String tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
        TrustManagerFactory tmf = TrustManagerFactory.getInstance(tmfAlgorithm);
        tmf.init(keyStore);

        SSLContext context = SSLContext.getInstance("TLS");
        context.init(null, tmf.getTrustManagers(), null);

        URL url = new URL(urlString);
        HttpsURLConnection urlConnection = (HttpsURLConnection)url.openConnection();
        urlConnection.setSSLSocketFactory(context.getSocketFactory());

        return urlConnection;
    }
    catch (Exception ex)
    {
        Log.e("fff", "Failed to establish SSL connection to server: " + ex.toString());
        ex.printStackTrace();
        return null;
    }

}
