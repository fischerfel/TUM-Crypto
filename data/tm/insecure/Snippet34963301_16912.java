public class AppController extends Application {

public static final String TAG = AppController.class.getSimpleName();

private RequestQueue mRequestQueue;

private static AppController mInstance;

@Override
public void onCreate() {
    super.onCreate();
    mInstance = this;
}

public static synchronized AppController getInstance() {
    return mInstance;
}



public RequestQueue getRequestQueue()
{
    if (mRequestQueue == null)
    {
         mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        Network network = new BasicNetwork(new OkHttpStack());
        mRequestQueue = new RequestQueue(new DiskBasedCache(new File(getCacheDir(), "volley")), network);
        mRequestQueue.start();
    }

    return mRequestQueue;
}
public class OkHttpStack extends HurlStack {
    private final OkUrlFactory mFactory;

    public OkHttpStack() {
        this(new OkHttpClient());
    }


    private  OkHttpClient getUnsafeOkHttpClient()
    {
        try {
            // Create a trust manager that does not validate certificate chains
            final TrustManager[] trustAllCerts = new TrustManager[]
                    {
                            new X509TrustManager() {
                                @Override
                                public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) {}

                                @Override
                                public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) {}

                                @Override
                                public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                                    return null;
                                }
                            }
                    };

            // Install the all-trusting trust manager
            final SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
            // Create an ssl socket factory with our all-trusting manager
            final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

            OkHttpClient okHttpClient = new OkHttpClient();
            okHttpClient.setSslSocketFactory(sslSocketFactory);
            okHttpClient.setHostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            });

            return okHttpClient;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }




    public OkHttpStack(OkHttpClient client)
    {
        client=getUnsafeOkHttpClient();
        if (client == null) {
            throw new NullPointerException("Client must not be null.");
        }
        mFactory = new OkUrlFactory(client);
    }

    @Override
    protected HttpURLConnection createConnection(URL url) throws IOException {
        return mFactory.open(url);
    }
}


public <T> void addToRequestQueue(Request<T> req, String tag) {
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
