SSLContext ctx = SSLContext.getInstance("TLS");
ctx.init(keyManagerFactory.getKeyManagers(), tmf.getTrustManagers(), null);
final ClientConfig config = new DefaultClientConfig();
config.getProperties().put(HTTPSProperties.PROPERTY_HTTPS_PROPERTIES, new HTTPSProperties(null, ctx));
Client create = Client.create(config);    
create.resource(targetUrl).post();
