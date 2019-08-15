public class VolleySingleton {


  private static VolleySingleton mInstance;
  private RequestQueue mRequestQueue;
  private static Context mCtx;

  private VolleySingleton(Context context) {
    mCtx = context;
    mRequestQueue = getRequestQueue();
  }

  public static synchronized VolleySingleton getInstance(Context context) {
    if (mInstance == null) {
      mInstance = new VolleySingleton(context);
    }
    return mInstance;
  }

  public RequestQueue getRequestQueue() {
    if (mRequestQueue == null) {
      // getApplicationContext() is key, it keeps you from leaking the
      // Activity or BroadcastReceiver if someone passes one in.
      mRequestQueue = Volley.newRequestQueue(mCtx.getApplicationContext(), new HurlStack(null, newSslSocketFactory()));
    }
    return mRequestQueue;
  }

  public <T> void addToRequestQueue(Request<T> req) {
    int socketTimeout = 90000;
    RetryPolicy policy = new DefaultRetryPolicy(socketTimeout,
        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
    req.setRetryPolicy(policy);
    getRequestQueue().add(req);
  }

  private SSLSocketFactory newSslSocketFactory() {
    try {
      // Get an instance of the Bouncy Castle KeyStore format
      KeyStore trusted = KeyStore.getInstance("BKS");
      // Get the raw resource, which contains the keystore with
      // your trusted certificates (root and any intermediate certs)
      InputStream in = mCtx.getApplicationContext().getResources().openRawResource(R.raw.trusted);
      try {
        // Initialize the keystore with the provided trusted certificates
        // Provide the password of the keystore
        trusted.load(in, mCtx.getString(R.string.KEYSTORE_PASS).toCharArray());
      } finally {
        in.close();
      }

      String tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
      TrustManagerFactory tmf = TrustManagerFactory.getInstance(tmfAlgorithm);
      tmf.init(trusted);

      SSLContext context = SSLContext.getInstance("TLSv1.2");
      context.init(null, tmf.getTrustManagers(), null);

      HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
        @Override
        public boolean verify(String hostname, SSLSession session) {
          Log.i("Volley","Verifing host:"+hostname);
          return true;
        }
      });

      SSLSocketFactory sf = context.getSocketFactory();
      return sf;
    } catch (Exception e) {
      throw new AssertionError(e);
    }
  }
}
