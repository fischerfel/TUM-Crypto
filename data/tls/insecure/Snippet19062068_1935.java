ClientConfig config = new DefaultClientConfig();
SSLContext ctx = SSLContext.getInstance("SSL");
ctx.init(null, myTrustManager, null);
config.getProperties().put(HTTPSProperties.PROPERTY_HTTPS_PROPERTIES, new HTTPSProperties(hostnameVerifier, ctx));
Client client = Client.create(config);
