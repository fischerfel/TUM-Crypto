public static void main(String[] args) throws Exception {
    CamelContext context = new DefaultCamelContext();

    JettyHttpComponent jetty = context.getComponent("jetty", JettyHttpComponent.class);

    SslSelectChannelConnector sslConnector = new SslSelectChannelConnector();
    sslConnector.setPort(9443);
    sslConnector.setKeystore("/home/brian/jboss.keystore");
    sslConnector.setKeyPassword("changeit");
    sslConnector.setTruststore("/home/brian/jboss.truststore");
    sslConnector.setTrustPassword("changeit");
    sslConnector.setPassword("changeit");
    sslConnector.setNeedClientAuth(true);

    Map<Integer, SslSelectChannelConnector> connectors = new HashMap<Integer, SslSelectChannelConnector>();
    connectors.put(9443, sslConnector);

    jetty.setSslSocketConnectors(connectors);

    final Endpoint jettyEndpoint = jetty.createEndpoint("jetty:https://localhost:9443/service");

    KeyStore keystore = KeyStore.getInstance("PKCS12");
    keystore.load(new FileInputStream(new File("/home/brian/User2.p12")), "Password1234!".toCharArray());
    X509KeyManager keyManager = new CTSKeyManager(keystore, "user2", "Password1234!".toCharArray());
    KeyManager[] keyManagers = new KeyManager[] { keyManager };

    X509TrustManager trustManager = new EasyTrustManager();
    TrustManager[] trustManagers = new TrustManager[] { trustManager };

    SSLContext sslcontext = SSLContext.getInstance("TLS");
    sslcontext.init(keyManagers, trustManagers, null);

    SchemeRegistry registry = new SchemeRegistry();
    registry.register(new Scheme("https", 443, new SSLSocketFactory(sslcontext,
            SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER)));

    HttpComponent http4 = context.getComponent("http4", HttpComponent.class);
    http4.setClientConnectionManager(new ThreadSafeClientConnManager(registry));
    final Endpoint https4Endpoint = http4
            .createEndpoint("https4://soafa-lite-staging:443/axis2/services/SigActService?bridgeEndpoint=true&throwExceptionOnFailure=false");
    context.addRoutes(new RouteBuilder() {

        @Override
        public void configure() {
            from(jettyEndpoint).to(https4Endpoint);
        }
    });

    context.start();

    context.stop();
}

private static class EasyTrustManager implements X509TrustManager {

    @Override
    public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
    }

    @Override
    public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
    }

    @Override
    public X509Certificate[] getAcceptedIssuers() {
        return null;
    }

};

private static class CTSKeyManager extends X509ExtendedKeyManager {
    private final KeyStore keystore;
    private final char[] privateKeyPassword;
    private final String privateKeyAlias;

    public CTSKeyManager(KeyStore keystore, String privateKeyAlias, char[] privateKeyPassword) {
        this.keystore = keystore;
        this.privateKeyAlias = privateKeyAlias;
        this.privateKeyPassword = privateKeyPassword;
    }

    @Override
    public String[] getServerAliases(String keyType, Principal[] issuers) {
        String[] serverAliases = null;
        try {
            List<String> aliasList = new ArrayList<String>();
            int count = 0;
            Enumeration<String> aliases = keystore.aliases();
            while (aliases.hasMoreElements()) {
                String alias = aliases.nextElement();
                aliasList.add(alias);
                count++;
            }
            serverAliases = aliasList.toArray(new String[count]);
        } catch (Exception e) {
        }
        return serverAliases;
    }

    @Override
    public PrivateKey getPrivateKey(String alias) {
        PrivateKey privateKey = null;
        try {
            privateKey = (PrivateKey) keystore.getKey(alias, privateKeyPassword);
        } catch (Exception e) {
        }
        return privateKey;
    }

    @Override
    public String[] getClientAliases(String keyType, Principal[] issuers) {
        return privateKeyAlias == null ? null : new String[] { privateKeyAlias };
    }

    @Override
    public X509Certificate[] getCertificateChain(String alias) {
        X509Certificate[] x509 = null;
        try {
            Certificate[] certs = keystore.getCertificateChain(alias);
            if (certs == null || certs.length == 0) {
                return null;
            }
            x509 = new X509Certificate[certs.length];
            for (int i = 0; i < certs.length; i++) {
                x509[i] = (X509Certificate) certs[i];
            }
        } catch (Exception e) {
        }
        return x509;
    }

    @Override
    public String chooseServerAlias(String keyType, Principal[] issuers, Socket socket) {
        return privateKeyAlias;
    }

    @Override
    public String chooseClientAlias(String[] keyType, Principal[] issuers, Socket socket) {
        return privateKeyAlias;
    }

    @Override
    public String chooseEngineClientAlias(String[] keyType, Principal[] issuers, SSLEngine engine) {
        return privateKeyAlias;
    }

    @Override
    public String chooseEngineServerAlias(String keyType, Principal[] issuers, SSLEngine engine) {
        return privateKeyAlias;
    }
}

}
