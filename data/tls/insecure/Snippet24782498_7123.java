public class CassandraClientDatastax {
private Cluster cluster;
private Session session;

public void connect(String node) throws Exception {
    SSLContext context =
            getSSLContext("client-truststore.jks", "cassandrapw",
                "client-keystore.jks", "cassandrapw");
    String[] cipherSuites = {
    "TLS_RSA_WITH_NULL_SHA256","SSL_RSA_WITH_NULL_MD5","SSL_RSA_WITH_NULL_SHA","TLS_RSA_WITH_AES_128_CBC_SHA"
                             }; 
    System.out.println("Building cluster *************  ");
    cluster =
            Cluster.builder().addContactPoints("localhost")
            .withPort(9042)
            .withSSL(new SSLOptions(context, cipherSuites))                
            .build();
}

private  SSLContext getSSLContext(String truststorePath, String truststorePassword, String keystorePath,
                                        String keystorePassword) throws Exception
{
    FileInputStream tsf = new FileInputStream(Thread.currentThread().getContextClassLoader().getResource((truststorePath)).getPath());
    FileInputStream ksf = new FileInputStream(Thread.currentThread().getContextClassLoader().getResource((keystorePath)).getPath());

    /*InputStream tsf = Thread.currentThread().getContextClassLoader().getResource((truststorePath));
    InputStream ksf = Thread.currentThread().getContextClassLoader().getResource((keystorePath));*/
    SSLContext ctx = SSLContext.getInstance("TLS");

    KeyStore ts = KeyStore.getInstance("JKS");
    ts.load(tsf, truststorePassword.toCharArray());
    TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
    tmf.init(ts);

    KeyStore ks = KeyStore.getInstance("JKS");
    ks.load(ksf, keystorePassword.toCharArray());
    KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
    kmf.init(ks, keystorePassword.toCharArray());

    ctx.init(kmf.getKeyManagers(), tmf.getTrustManagers(), new SecureRandom());
    System.out.println("SSL Context Build Done ...................");
    return ctx;
}
