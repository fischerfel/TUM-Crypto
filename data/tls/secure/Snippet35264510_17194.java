final SSLContext sslContext = SSLContext.getInstance("TLSv1.2");
        sslContext.init(null, null, null);

final SchemeRegistry schemeRegistry = new SchemeRegistry();


final Scheme scheme = new Scheme("https", sslContext.getSocketFactory(), 443);
schemeRegistry.register(scheme);

final BasicClientConnectionManager cm = new BasicClientConnectionManager(schemeRegistry);
final DefaultHttpClient httpClient = new DefaultHttpClient(cm);
httpPost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
final HttpResponse response = httpClient.execute(httpPost);
