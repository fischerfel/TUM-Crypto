@Test
public void shouldGetSslSocketFactory() throws IOException, NoSuchAlgorithmException, KeyManagementException{
    String trustStoreFilePath = System.getProperty("javax.net.ssl.trustStore");

    TrustManagerFactory trustManagerFactory = CertManagerFactory.loadTrustStore(trustStoreFilePath);
    SSLContext sslContext = SSLContext.getInstance("TLS");
    sslContext.init(null, trustManagerFactory.getTrustManagers(), null);
    this.sslSocketFactory = sslContext.getSocketFactory();

    assertEquals(sslSocketFactory, clientCertSocketFactory.getSslSocketFactory());


}
