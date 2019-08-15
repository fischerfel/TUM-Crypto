 public static SSLContext getSSLContext() throws NoSuchAlgorithmException, KeyManagementException, KeyStoreException, CertificateException, NotFoundException, IOException, UnrecoverableKeyException{
    KeyStore clientCertificateKeysKeyStore = getClientCertificateKeystore();    
    KeyStore trustStore = getServerCertificateKeystore();
    KeyManagerFactory kmf = KeyManagerFactory.getInstance(X509);    

    if(clientCertificateKeysKeyStore != null)
        kmf.init(clientCertificateKeysKeyStore, "cleint".toCharArray());
    KeyManager[] keyManagers = kmf.getKeyManagers();


//  TrustManager[] trustManagers = {new CustomTrustManager(trustStore)};
    TrustManagerFactory tmf = TrustManagerFactory.getInstance(X509);
    tmf.init(trustStore);

    TrustManager[] trustManagers = tmf.getTrustManagers();

    SSLContext sslContext = SSLContext.getInstance("TLS");
    sslContext.init(keyManagers, trustManagers, null);
    return sslContext;

}
