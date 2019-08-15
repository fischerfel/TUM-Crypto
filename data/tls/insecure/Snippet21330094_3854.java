//use java. ... .X509Certificate, not javax. ... .X509Certificate
import java.security.cert.X509Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;

@Test
public void testSslCertificate()
        throws IOException, KeyStoreException, NoSuchAlgorithmException,
               CertificateException, KeyManagementException {

    X509Certificate cert;
    try (FileInputStream pemFileStream = new FileInputStream(newFile("your.pem"))) {
        CertificateFactory certFactory = CertificateFactory.getInstance("X509");
        cert = (X509Certificate) certFactory.generateCertificate(pemFileStream);
    }

    //create truststore
    KeyStore trustStore = KeyStore.getInstance("JKS");
    trustStore.load(null); //create an empty trustore

    //add certificate to truststore - you can use a simpler alias
    String alias = cert.getSubjectX500Principal().getName() + "["
            + cert.getSubjectX500Principal().getName().hashCode() + "]";
    trustStore.setCertificateEntry(alias, cert);

    //configure http client
    TrustManagerFactory trustManagerFactory =
       TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
    trustManagerFactory.init(trustStore);

    SSLContext sslContext = SSLContext.getInstance("TLS");
    sslContext.init(null, trustManagerFactory.getTrustManagers(), null);

    HttpClientBuilder httpClientBuilder = HttpClientBuilder.
                                          create().setSslcontext(sslContext);

    try (CloseableHttpClient httpClient = httpClientBuilder.build()) {
        HttpGet httpGetRequest = new HttpGet("https://yourServer");
        try (CloseableHttpResponse httpResponse =
                            httpClient.execute(httpGetRequest)) {
            Assert.assertEquals(200,
                                httpResponse.getStatusLine().getStatusCode());
        }
    }
}
