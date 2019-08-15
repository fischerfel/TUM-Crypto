KeyStore keyStore = KeyStore.getInstance("BKS");
InputStream in = getContext().getAssets().open(Constants.KEYSTORE_FILENAME);
keyStore.load(in, Constants.KEYSTORE_PASSWORD);
in.close();

SSLSocketFactory socketFactory = new SSLSocketFactory(keyStore);
socketFactory.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
SchemeRegistry registry = new SchemeRegistry();
registry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
registry.register(new Scheme("https", socketFactory, 443));
BasicHttpParams basicHttpParams = new BasicHttpParams();
HttpProtocolParams.setVersion(basicHttpParams, HttpVersion.HTTP_1_1);
HttpProtocolParams.setContentCharset(basicHttpParams, HTTP.UTF_8);
ThreadSafeClientConnManager ccm = new ThreadSafeClientConnManager(basicHttpParams, registry);

httpClient = new DefaultHttpClient(ccm, basicHttpParams);
