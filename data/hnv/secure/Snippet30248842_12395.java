DefaultHttpClient ret = null;
//SETS UP PARAMETERS
HttpParams params = new BasicHttpParams();
HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
HttpProtocolParams.setContentCharset(params, "utf-8");
params.setBooleanParameter("http.protocol.expect-continue", false);

//REGISTERS SCHEMES FOR BOTH HTTP AND HTTPS
SchemeRegistry registry = new SchemeRegistry();
registry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));

final SSLSocketFactory sslSocketFactory = new MySSLSocketFactory(sslContext);
//final SSLSocketFactory sslSocketFactory = SSLSocketFactory.getSocketFactory();

sslSocketFactory.setHostnameVerifier(SSLSocketFactory.STRICT_HOSTNAME_VERIFIER);
registry.register(new Scheme("https", sslSocketFactory, 443));

ThreadSafeClientConnManager manager = new ThreadSafeClientConnManager(params, registry);
return ret = new DefaultHttpClient(manager, params);
