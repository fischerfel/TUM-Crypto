System.setProperty("javax.net.ssl.trustStore", "truststore.jks");
System.setProperty("javax.net.ssl.trustStoreType", "JKS");
KeyStore trustStore = KeyStore.getInstance("JKS");
trustStore.load(new FileInputStream("truststore.jks"),null);
TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
trustManagerFactory.init(trustStore);

SSLContext ctx = SSLContext.getInstance("SSL");
ctx.init(null, trustManagerFactory.getTrustManagers(), null);

ExchangeService service = new ExchangeService(ExchangeVersion.Exchange2010_SP1);
service.setTraceEnabled(true);
service.setPreAuthenticate(true);
ExchangeCredentials credentials = new WebCredentials(username,password,domain);
service.setCredentials(credentials);
service.setUserAgent(user);
service.setUrl(new java.net.URI("https://autodiscover.company.com/xxxx/autodiscover.svc"));
