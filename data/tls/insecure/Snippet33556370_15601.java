@Autowired
private Environment env;

@Bean
public CassandraClusterFactoryBean cluster() {
    SSLContext context = null;

    try {
        context = getSSLContext(
                env.getProperty("cassandra.connection.ssl.trustStorePath"),
                env.getProperty("cassandra.connection.ssl.trustStorePassword"),
                env.getProperty("cassandra.connection.ssl.keyStorePath"),
                env.getProperty("cassandra.connection.ssl.keyStorePassword"));
    } catch (Exception ex) {
        log.warn("Error setting SSL context for Cassandra.");
    }

    // Default cipher suites supported by C*
    String[] cipherSuites = { "TLS_RSA_WITH_AES_128_CBC_SHA",
            "TLS_RSA_WITH_AES_256_CBC_SHA" };

    CassandraClusterFactoryBean cluster = new CassandraClusterFactoryBean();
    cluster.setContactPoints(env.getProperty("cassandra.contactpoints"));
    cluster.setPort(Integer.parseInt(env.getProperty("cassandra.port")));
    cluster.setSslOptions(new SSLOptions(context, cipherSuites));
    cluster.setSslEnabled(true);
    return cluster;
}

@Bean
public CassandraMappingContext mappingContext() {
    return new BasicCassandraMappingContext();
}

@Bean
public CassandraConverter converter() {
    return new MappingCassandraConverter(mappingContext());
}

@Bean
public CassandraSessionFactoryBean session() throws Exception {

    CassandraSessionFactoryBean session = new CassandraSessionFactoryBean();
    session.setCluster(cluster().getObject());
    session.setKeyspaceName(env.getProperty("cassandra.keyspace"));
    session.setConverter(converter());
    session.setSchemaAction(SchemaAction.NONE);

    return session;
}

@Bean
public CassandraOperations cassandraTemplate() throws Exception {
    return new CassandraTemplate(session().getObject());
}

private static SSLContext getSSLContext(String truststorePath,
        String truststorePassword, String keystorePath,
        String keystorePassword) throws Exception {
    FileInputStream tsf = new FileInputStream(truststorePath);
    FileInputStream ksf = new FileInputStream(keystorePath);
    SSLContext ctx = SSLContext.getInstance("SSL");

    KeyStore ts = KeyStore.getInstance("JKS");
    ts.load(tsf, truststorePassword.toCharArray());
    TrustManagerFactory tmf = TrustManagerFactory
            .getInstance(TrustManagerFactory.getDefaultAlgorithm());
    tmf.init(ts);

    KeyStore ks = KeyStore.getInstance("JKS");
    ks.load(ksf, keystorePassword.toCharArray());
    KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory
            .getDefaultAlgorithm());
    kmf.init(ks, keystorePassword.toCharArray());

    ctx.init(kmf.getKeyManagers(), tmf.getTrustManagers(),
            new SecureRandom());
    return ctx;
}
