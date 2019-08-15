public class CamelProxy {

/**
 * @param args
 */
public static void main(String[] args) throws Exception {
    CamelContext context = new DefaultCamelContext();

    final Endpoint jettyEndpoint = configureJetty(context);

    final Endpoint https4Endpoint = configureHttpClient(context);

    context.addRoutes(new RouteBuilder() {

        @Override
        public void configure() {
            from(jettyEndpoint).to("log:com.smithforge.request?showAll=true").to(https4Endpoint);
        }
    });

    context.start();

    context.stop();
}

private static Endpoint configureHttpClient(CamelContext context) throws Exception {
    KeyStore keystore = KeyStore.getInstance("PKCS12");
    keystore.load(new FileInputStream(new File("/home/brian/User2.p12")), "Password1234!".toCharArray());

    KeyStore truststore = KeyStore.getInstance("JKS");
    truststore.load(new FileInputStream(new File("/home/brian/jboss.truststore")), "changeit".toCharArray());

    KeyManagerFactory keyFactory = KeyManagerFactory.getInstance("SunX509");
    keyFactory.init(keystore, "Password1234!".toCharArray());

    TrustManagerFactory trustFactory = TrustManagerFactory.getInstance("SunX509");
    trustFactory.init(truststore);

    SSLContext sslcontext = SSLContext.getInstance("TLSv1");
    sslcontext.init(keyFactory.getKeyManagers(), trustFactory.getTrustManagers(), null);

    SSLSocketFactory factory = new SSLSocketFactory(sslcontext, SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);

    SchemeRegistry registry = new SchemeRegistry();
    final Scheme scheme = new Scheme("https4", 443, factory);
    registry.register(scheme);

    HttpComponent http4 = context.getComponent("http4", HttpComponent.class);
    http4.setHttpClientConfigurer(new HttpClientConfigurer() {

        @Override
        public void configureHttpClient(HttpClient client) {
            client.getConnectionManager().getSchemeRegistry().register(scheme);
        }

    });
    http4.setClientConnectionManager(new ThreadSafeClientConnManager());
    return http4
            .createEndpoint("https4://soafa-lite-staging:443/axis2/services/SigActService?bridgeEndpoint=true&throwExceptionOnFailure=false");
}

private static Endpoint configureJetty(CamelContext context) throws Exception {
    JettyHttpComponent jetty = context.getComponent("jetty", JettyHttpComponent.class);

    SslSelectChannelConnector sslConnector = new SslSelectChannelConnector();
    sslConnector.setPort(4443);
    sslConnector.setKeystore("/home/brian/jboss.keystore");
    sslConnector.setKeyPassword("changeit");
    sslConnector.setTruststore("/home/brian/jboss.truststore");
    sslConnector.setTrustPassword("changeit");
    sslConnector.setPassword("changeit");
    sslConnector.setNeedClientAuth(true);
    sslConnector.setAllowRenegotiate(true);

    Map<Integer, SslSelectChannelConnector> connectors = new HashMap<Integer, SslSelectChannelConnector>();
    connectors.put(4443, sslConnector);

    jetty.setSslSocketConnectors(connectors);
    return jetty.createEndpoint("jetty:https://localhost:4443/service");
}

// .to("log:com.smithforge.response?showHeaders=true");
}
