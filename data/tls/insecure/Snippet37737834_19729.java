    public class Application extends android.app.Application {


    private static Application mInstance;
    private RequestQueue mRequestQueue;
    private ImageLoader mImageLoader;
    private static char[] KEYSTORE_PASSWORD = "password".toCharArray();

    private PreferenceManager pref;

    public static final String TAG = Application.class.getSimpleName();
    @Override
    public void onCreate() {
        mInstance = this;
      super.onCreate();
    }


    public static synchronized Application getInstance() {
        return mInstance;
    }

    @Override
    public void onTerminate() {
        FontUtils.clean();
        super.onTerminate();
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            //mRequestQueue = Volley.newRequestQueue(getApplicationContext());
            mRequestQueue = Volley.newRequestQueue(getApplicationContext(), new HurlStack(null, newSslSocketFactory()));
            mRequestQueue.start();
        }

        return mRequestQueue;
    }

    public ImageLoader getImageLoader() {
        getRequestQueue();
        if (mImageLoader == null) {
            mImageLoader = new ImageLoader(this.mRequestQueue,
                    new LruBitmapCache());
        }
        return this.mImageLoader;
    }


    public PreferenceManager getPrefManager() {
        if (pref == null) {
            pref = new PreferenceManager(this);
        }

        return pref;
    }



    public <T> void addToRequestQueue(Request<T> req, String tag) {
        // set the default tag if tag is empty
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(req);
    }

    public <T> void addToRequestQueue(Request<T> req) {
        req.setTag(TAG);
        getRequestQueue().add(req);
    }

    public void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }


    private static SSLSocketFactory newSslSocketFactory() {
        try {
            KeyStore trusted = KeyStore.getInstance("BKS"); //PKCS7
            InputStream in = Application.getInstance().getResources().openRawResource(R.raw.keystore);

            try {
                trusted.load(in, KEYSTORE_PASSWORD);
            } finally {
                in.close();
            }

            String tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
            TrustManagerFactory tmf = TrustManagerFactory.getInstance(tmfAlgorithm);
            tmf.init(trusted);

            SSLContext context = SSLContext.getInstance("TLS");

            HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {

                @Override
                public boolean verify(String arg0, SSLSession arg1) {
                    Log.e(TAG, "ARG =" + arg0 + " == arg1" + arg1);
                    return true;
                }
            });
            context.init(null, tmf.getTrustManagers(), null);
            SSLSocketFactory sf = context.getSocketFactory();
            return sf;
        } catch (Exception e) {
            throw new AssertionError(e);
        }
    } 

}
