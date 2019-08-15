private HttpsURLConnection getHttpsURLConnection(URL url, String certAlias)
        throws CertificateException, IOException, KeyStoreException, NoSuchAlgorithmException, KeyManagementException, UnrecoverableKeyException, java.security.cert.CertificateException {
    HttpsURLConnection connection = null;
    CertificateFactory certFactory = null;
    java.security.cert.Certificate cert = null;
    KeyStore keyStore = null;
    TrustManagerFactory tmFactory = null;
    SSLContext sslContext = null;

    //load client cert
    final KeyStore clientKS = getClientKeyStore();
    KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
    kmf.init(keyStore, clientCertificatePassword.toCharArray());

    // Load certificates from an InputStream
    certFactory = CertificateFactory.getInstance("X.509");

    InputStream servCertIS = new FileInputStream(caCertificateName);
    cert = certFactory.generateCertificate(servCertIS);

    // Create a KeyStore containing the trusted certificates
    keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
    keyStore.load(null, null);
    keyStore.setCertificateEntry(certAlias, cert);

    // Create a TrustManager that trusts the certificates in our KeyStore
    tmFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
    tmFactory.init(keyStore);
    // Create an SSLContext that uses our TrustManager
    sslContext = SSLContext.getInstance("TLS");

    sslContext.init(kmf.getKeyManagers(), tmFactory.getTrustManagers(), null);

    connection = (HttpsURLConnection)url.openConnection();
    connection.setSSLSocketFactory(sslContext.getSocketFactory());
    InputStream in = connection.getInputStream(); //error!
    //peoccess inputStream here.

    return connection;
