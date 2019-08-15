public HttpClient() {

    /* ... */

    SSLContext sslContext = null;

    try {
        sslContext = SSLContext.getInstance("TLSv1.2");
    } catch (NoSuchAlgorithmException e) {
        e.printStackTrace();
    }

    KeyStore keyStore = readKeyStore();

    TrustManagerFactory trustManagerFactory = null;
    try {
        trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        trustManagerFactory.init(keyStore);
        KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
        keyManagerFactory.init(keyStore, "password".toCharArray());
        sslContext.init(keyManagerFactory.getKeyManagers(), trustManagerFactory.getTrustManagers(), new SecureRandom());
    }
    catch (final Exception e) {
        Log.e(TAG, e.toString());
    }


    TrustManager[] trustManagers = trustManagerFactory.getTrustManagers();
    X509TrustManager trustManager = (X509TrustManager) trustManagers[0];


    OkHttpClient client = new OkHttpClient.Builder()
            .sslSocketFactory(sslContext.getSocketFactory(), trustManager)
            .build();

    mRetrofit = new Retrofit.Builder()
            .client(client)
            .baseUrl(mBaseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build();
}

private KeyStore readKeyStore() {

    KeyStore ks = null;
    try {
        ks = KeyStore.getInstance("BKS");
    }
    catch (final Exception e) {
        Log.e(TAG, e.toString());
    }

    char[] password = "password".toCharArray();

    final Context context = App.app;
    InputStream is = context.getResources().openRawResource(R.raw.key_sorage);

    try {
        ks.load(is, password);
    }
    catch (final Exception e) {
        Log.e(TAG, e.toString());
    }
    finally {
        if (is != null) {
            try {
                is.close();
            }
            catch (final Exception e2) {
                Log.e(TAG, e2.toString());
            }
        }
    }

    return ks;
}
