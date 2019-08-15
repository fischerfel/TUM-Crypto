SSLContext sslContext = SSLContext.getInstance("TLSv1.1");
sslContext.init(
    keymanagers.toArray(new KeyManager[keymanagers.size()]),
    null,
    null);

SSLSocketFactory socketFactory = new SSLSocketFactory(sslContext, new String[]{"TLSv1.1"}, null, null);
Scheme scheme = new Scheme("https", 443, socketFactory);
SchemeRegistry schemeRegistry = new SchemeRegistry();
schemeRegistry.register(scheme);
BasicClientConnectionManager cm = new BasicClientConnectionManager(schemeRegistry);
httpClient = new DefaultHttpClient(cm);
