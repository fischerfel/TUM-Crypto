  public static HttpsURLConnection connectSelfSignedHttps(Context ctx, String surl) throws CertificateException, IOException, KeyStoreException, NoSuchAlgorithmException, KeyManagementException {

    // Load CAs from an InputStream
    // (could be from a resource or ByteArrayInputStream or ...)
    CertificateFactory cf = CertificateFactory.getInstance("X.509");

    // 
    InputStream caInput = new BufferedInputStream(ctx.getApplicationContext().getAssets().open("my.server.net.crt"));
    Certificate ca;
    try {
      ca = cf.generateCertificate(caInput);
      //System.out.println("ca=" + ((X509Certificate) ca).getSubjectDN());
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
    SSLContext context = SSLContext.getInstance("TLS");
    context.init(null, tmf.getTrustManagers(), null);

    // Tell the URLConnection to use a SocketFactory from our SSLContext
    URL url = new URL(surl);
    HttpsURLConnection urlConnection =
        (HttpsURLConnection)url.openConnection();
    urlConnection.setSSLSocketFactory(context.getSocketFactory());

    return urlConnection;
  }
