try
{
    SSLContext context = null;

    CertificateFactory cf = CertificateFactory.getInstance("X.509");
    InputStream caInput = getResources().openRawResource(R.raw.mi_net);
    Certificate ca;
    try {
        ca = cf.generateCertificate(caInput);
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
    context = SSLContext.getInstance("TLS");
    context.init(null, tmf.getTrustManagers(), null);

    url = new URL(urlStr);
    HttpsURLConnection con = (HttpsURLConnection) url.openConnection();  
    con.setSSLSocketFactory(context.getSocketFactory());
    con.setInstanceFollowRedirects(true);

    con.setDoOutput(false);
    con.setConnectTimeout(1000);
    String responseMsg = con.getResponseMessage();
    response = con.getResponseCode();
    is = con.getInputStream();
}
