System.setProperty("https.protocols","TLSv1");
SSLContext sslContext = SSLContext.getInstance("TLSv1");
sslContext.init(null, null, null);

SSLSocketFactory sslSocketFactory = new SSLSocketFactory(sslContext, SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
Scheme httpsScheme = new Scheme("https", 443, sslSocketFactory);

SchemeRegistry schemeRegistry = new SchemeRegistry();
schemeRegistry.register(httpsScheme);

ClientConnectionManager cm = new SingleClientConnManager(schemeRegistry);

HttpClient httpClient = new DefaultHttpClient(cm);
