 * This method set the certificate for the HttpsURLConnection
 * @param url The url to contact.
 * @param certificateInputStream The InputStream generated from the .crt certificate.
 * @param certAlias The alias for the certificate. 
 * @return The returned HttpsURLConnection
private HttpsURLConnection getHttpsURLConnection(URL url, InputStream certificateInputStream, String certAlias) 
    throws CertificateException, IOException, KeyStoreException, NoSuchAlgorithmException, KeyManagementException {
    HttpsURLConnection connection = null;
    CertificateFactory certFactory = null;
    Certificate cert = null;
    KeyStore keyStore = null;
    TrustManagerFactory tmFactory = null;
    SSLContext sslContext = null;
    // Load certificates from an InputStream
    certFactory = CertificateFactory.getInstance("X.509");
    cert = certFactory.generateCertificate(certificateInputStream);
    certificateInputStream.close();
    // Create a KeyStore containing the trusted certificates
    keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
    keyStore.load(null, null);
    keyStore.setCertificateEntry(certAlias, cert);
    // Create a TrustManager that trusts the certificates in our KeyStore
    tmFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
    tmFactory.init(keyStore);
    // Create an SSLContext that uses our TrustManager
    sslContext = SSLContext.getInstance("TLS");
    sslContext.init(null, tmFactory.getTrustManagers(), null);
    connection = (HttpsURLConnection)url.openConnection();
    connection.setSSLSocketFactory(sslContext.getSocketFactory());
    return connection;
}
