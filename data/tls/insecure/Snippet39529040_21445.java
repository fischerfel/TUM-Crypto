try {
    /*** CA Certificate ***/

    CertificateFactory cf = CertificateFactory.getInstance("X.509");
    InputStream caInput = getResources().openRawResource(R.raw.caserver);
    Certificate ca = cf.generateCertificate(caInput);
    System.out.println("ca=" + ((X509Certificate) ca).getSubjectDN());

    // Create a KeyStore containing our trusted CAs
    String keyStoreType = KeyStore.getDefaultType();
    KeyStore keyStore = KeyStore.getInstance(keyStoreType);
    keyStore.load(null, null);
    keyStore.setCertificateEntry("ca", ca);
    System.out.println(keyStoreType);

    // Create a TrustManager that trusts the CAs in our KeyStore
    String tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
    TrustManagerFactory tmf = TrustManagerFactory.getInstance(tmfAlgorithm);
    tmf.init(keyStore);

    /*** Client Certificate ***/

    KeyStore keyStore12 = KeyStore.getInstance("PKCS12");
    InputStream certInput12 = getResources().openRawResource(R.raw.p12client);
    keyStore12.load(certInput12, "123456key".toCharArray());

    // Create a KeyManager that uses our client cert
    String algorithm = KeyManagerFactory.getDefaultAlgorithm();
    KeyManagerFactory kmf = KeyManagerFactory.getInstance(algorithm);
    kmf.init(keyStore12, null);


    /*** SSL Connection ***/

    // Create an SSLContext that uses our TrustManager and our KeyManager
    SSLContext context = SSLContext.getInstance("TLS");
    context.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);

    URL url = new URL("https://myurl/test.json");
    HttpsURLConnection urlConnection = (HttpsURLConnection) url.openConnection();
    urlConnection.setSSLSocketFactory(context.getSocketFactory());

    System.out.println("Weeeeeeeeeee");
    InputStream in = urlConnection.getInputStream(); // this throw exception
}
catch (Exception e) {
    e.printStackTrace();
}
