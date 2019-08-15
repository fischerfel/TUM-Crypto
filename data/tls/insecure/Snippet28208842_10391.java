SSLContext ctx = SSLContext.getInstance("TLS");
ctx.init(keyManagerFactory.getKeyManagers(), tmf.getTrustManagers(), null);
final DefaultApacheHttpClientConfig apacheConfig = new DefaultApacheHttpClientConfig();
final Map<String, Object> properties = apacheConfig.getProperties();
properties.put(DefaultApacheHttpClientConfig.PROPERTY_PROXY_URI, "http://" + proxyHost + ":" + proxyPort);
properties.put(HTTPSProperties.PROPERTY_HTTPS_PROPERTIES, new HTTPSProperties(null,ctx));   
apacheConfig.getState().setProxyCredentials(AuthScope.ANY_REALM, proxyHost,   Integer.parseInt(proxyPort),proxyUser, proxyPassword);
Client create = ApacheHttpClient.create(apacheConfig);
create.resource(targetUrl).post();
