BasicHttpParams clientparams = new BasicHttpParams();
[... clientparams initializazion...]

SchemeRegistry schemeRegistry = new SchemeRegistry();
schemeRegistry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));

SSLSocketFactory sf = SSLSocketFactory.getSocketFactory();
sf.setHostnameVerifier(SSLSocketFactory.STRICT_HOSTNAME_VERIFIER);
schemeRegistry.register(new Scheme("https", sf, 443));

ClientConnectionManager cm = new ThreadSafeClientConnManager(clientparams, schemeRegistry);
HttpClient myClient = new DefaultHttpClient(cm, clientparams);
HttpResponse response = myClient.execute(new HttpGet("https://www.google.com"));
