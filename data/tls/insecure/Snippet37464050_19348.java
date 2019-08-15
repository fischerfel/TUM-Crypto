TrustManager[] trustManagers = new TrustManager[] { new DummyTrustManager() };

SSLContext sslContext = SSLContext.getInstance("TLS");
sslContext.init(keyManagers, trustManagers, null);

SSLSocketFactory sf = new SSLSocketFactory(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);

Scheme scheme = new Scheme("https", 443, sf);
SchemeRegistry registry = new SchemeRegistry();
registry.register(scheme);

ThreadSafeClientConnManager cm = new ThreadSafeClientConnManager(registry);

DefaultHttpClient client = new DefaultHttpClient(cm, httpParameters);
