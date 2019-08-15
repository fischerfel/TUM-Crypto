KeyStore trustStore = KeyStore.getInstance("BKS");
InputStream is = this.getAssets().open("discretio.bks");
trustStore.load(is, "discretio".toCharArray());
is.close();

SSLSocketFactory sockfacto = new SSLSocketFactory(trustStore);
sockfacto.setHostnameVerifier(SSLSocketFactory.STRICT_HOSTNAME_VERIFIER);

SchemeRegistry schemeRegistry = new SchemeRegistry();
schemeRegistry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
schemeRegistry.register(new Scheme("https", sockfacto, 443));

SingleClientConnManager mgr = new SingleClientConnManager(httpParameters, schemeRegistry);

HttpClient client = new DefaultHttpClient(mgr, httpParameters);
HttpGet request = new HttpGet(url);
HttpResponse response = client.execute(request);
