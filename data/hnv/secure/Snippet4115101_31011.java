 public static final String USER_AGENT = "Mozilla/5.0 (Windows; U; Windows NT 5.1; en-US; rv:1.9.2.8) Gecko/20100722 Firefox/3.6.8 (.NET CLR 3.5.30729)";

  public static class MyHttpClient extends DefaultHttpClient {

    final Context context;

    public MyHttpClient(Context context) {
      super();
      this.context = context;
    }

    @Override
    protected ClientConnectionManager createClientConnectionManager() {
      SchemeRegistry registry = new SchemeRegistry();
      registry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
      // Register for port 443 our SSLSocketFactory with our keystore
      // to the ConnectionManager
      registry.register(new Scheme("https", newSslSocketFactory(), 443));
      return new SingleClientConnManager(getParams(), registry);
    }

    private SSLSocketFactory newSslSocketFactory() {
      try {
        // Get an instance of the Bouncy Castle KeyStore format
        KeyStore trusted = KeyStore.getInstance("BKS");
        // Get the raw resource, which contains the keystore with
        // your trusted certificates (root and any intermediate certs)
        InputStream in = context.getResources().openRawResource(R.raw.battlenetkeystore);
        try {
          // Initialize the keystore with the provided trusted certificates
          // Also provide the password of the keystore
          trusted.load(in, "mysecret".toCharArray());
        } finally {
          in.close();
        }
        // Pass the keystore to the SSLSocketFactory. The factory is responsible
        // for the verification of the server certificate.
        SSLSocketFactory sf = new SSLSocketFactory(trusted);
        // Hostname verification from certificate
        // http://hc.apache.org/httpcomponents-client-ga/tutorial/html/connmgmt.html#d4e506
        sf.setHostnameVerifier(SSLSocketFactory.STRICT_HOSTNAME_VERIFIER);
        return sf;
      } catch (Exception e) {
        throw new AssertionError(e);
      }
    }
  }

  private static void maybeCreateHttpClient(Context context) {
    if (mHttpClient == null) {
      mHttpClient = new MyHttpClient(context);

      final HttpParams params = mHttpClient.getParams();
      HttpConnectionParams.setConnectionTimeout(params, REGISTRATION_TIMEOUT);
      HttpConnectionParams.setSoTimeout(params, REGISTRATION_TIMEOUT);
      ConnManagerParams.setTimeout(params, REGISTRATION_TIMEOUT);
      Log.d(TAG, LEAVE + "maybeCreateHttpClient()");
    }
  }

public static boolean authenticate(String username, String password, Handler handler,
      final Context context) {

    final HttpResponse resp;

    final ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
    params.add(new BasicNameValuePair(PARAM_USERNAME, username));
    params.add(new BasicNameValuePair(PARAM_PASSWORD, password));

    HttpEntity entity = null;
    try {
      entity = new UrlEncodedFormEntity(params);
    } catch (final UnsupportedEncodingException e) {
      // this should never happen.
      throw new AssertionError(e);
    }

    final HttpPost post = new HttpPost(THE_URL);
    post.addHeader(entity.getContentType());
    post.addHeader("User-Agent", USER_AGENT);
    post.setEntity(entity);

    maybeCreateHttpClient(context);

    if (mHttpClient == null) {
      return false;
    }

    try {
      resp = mHttpClient.execute(post);
    } catch (final IOException e) {
      Log.e(TAG, "IOException while authenticating", e);
      return false;
    } finally {
    }
}
