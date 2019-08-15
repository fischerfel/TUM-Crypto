System.setProperty("javax.net.ssl.trustStore", "truststore.jks");
System.setProperty("javax.net.ssl.trustStoreType", "JKS");

KeyStore trustStore = KeyStore.getInstance("JKS");
trustStore.load(new FileInputStream("truststore.jks"), null);
TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        trustManagerFactory.init(trustStore);

SSLContext ctx = SSLContext.getInstance("SSL");
ctx.init(null, trustManagerFactory.getTrustManagers(), null);

HostnameVerifier hostnameVerifier = HttpsURLConnection.getDefaultHostnameVerifier();
ClientConfig config = new com.sun.jersey.api.client.config.DefaultClientConfig();
client = Client.create(config);
config.getProperties().put(HTTPSProperties.PROPERTY_HTTPS_PROPERTIES, new HTTPSProperties(hostnameVerifier, ctx));
webResource = client.resource("https://autodiscover.company.com/xxxx/autodiscover.svc");
client.addFilter(new HTTPBasicAuthFilter(username, password));
