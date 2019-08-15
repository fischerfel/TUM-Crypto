private static HttpClient getNewHttpClient() {
try {
KeyStore trustStore = KeyStore.getInstance("BKS");
              InputStream trustStoreStream = SampleApp.appContext.getResources().openRawResource(R.raw.server);
              trustStore.load(trustStoreStream, "A123456a".toCharArray());

              SSLSocketFactory sslf = new SSLSocketFactory(trustStore);
              sslf.setHostnameVerifier(SSLSocketFactory.STRICT_HOSTNAME_VERIFIER);
              SchemeRegistry schemeRegistry = new SchemeRegistry();
              schemeRegistry.register(new Scheme ("https", sslf, 443));

              HttpParams params = new BasicHttpParams();
              HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
              HttpProtocolParams.setContentCharset(params, HTTP.UTF_8);
              HttpConnectionParams.setConnectionTimeout(params, 20000000);
              HttpConnectionParams.setSoTimeout(params, 20000000);

              SingleClientConnManager cm = new SingleClientConnManager(params, schemeRegistry);

              return new DefaultHttpClient(cm, params);
 } catch (Exception e) {
          e.printStackTrace();
          return new DefaultHttpClient();
      }
  }
