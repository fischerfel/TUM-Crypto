  private AsyncHttpClient asyncHttpClient;
    private AppPreference mAppPreferences;
    private UserPreference mUserPreference;
    public CLHttpClient(Context context) {
        this.context = context;
        MySSLSocketFactory socketFactory = null;
        try {
            KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
            trustStore.load(null, null);
            socketFactory = new MySSLSocketFactory(trustStore);
        } catch (Exception e) {
            e.printStackTrace();
        }

        asyncHttpClient = new AsyncHttpClient();
        asyncHttpClient.setTimeout(30 * 1000);
        if (socketFactory != null) {
            socketFactory.setHostnameVerifier(MySSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
            asyncHttpClient.setSSLSocketFactory(socketFactory);
        }
        asyncHttpClient.setMaxRetriesAndTimeout(1, 30000);
        asyncHttpClient.setUserAgent("android-async-http-1.4.9");
        mAppPreferences = new AppPreference(context);
        mUserPreference = new UserPreference(context);
    }
