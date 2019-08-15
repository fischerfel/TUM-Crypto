public static OkHttpClient getUnsafeOkHttpClient() {

    try {
        File mFolder = new File(Environment.getExternalStorageDirectory() + "/certificate");
        if (!mFolder.exists())
        {
            mFolder.mkdir();
        }

        String fileName = "certificate1a.cer";

        File file = new File(mFolder, fileName);

        FileInputStream fis = null;

        fis = new FileInputStream(file);

        CertificateFactory cf = CertificateFactory.getInstance("X.509");


        Certificate ca = cf.generateCertificate(fis);

        // Create a KeyStore containing our trusted CAs
        String keyStoreType = KeyStore.getDefaultType();
        KeyStore keyStore = KeyStore.getInstance(keyStoreType);
        keyStore.load(null, null);
        keyStore.setCertificateEntry("ca", ca);

        // Create a TrustManager that trusts the CAs in our KeyStore
        String tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
        TrustManagerFactory tmf = TrustManagerFactory.getInstance(tmfAlgorithm);
        tmf.init(keyStore);

        // Install the all-trusting trust manager
        final SSLContext sslContext = SSLContext.getInstance("TSL");
        sslContext.init(null, tmf.getTrustManagers(),null);
        // Create an ssl socket factory with our all-trusting manager
        final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

        OkHttpClient okHttpClient = new OkHttpClient();
        okHttpClient.setSslSocketFactory(sslSocketFactory);
        okHttpClient.interceptors().add(new APIRequestInterceptor());
        okHttpClient.interceptors().add(new APIResponseInterceptor());

        okHttpClient.setHostnameVerifier(new HostnameVerifier() {
            @Override
            public boolean verify(String hostname, SSLSession sslSession) {
                HostnameVerifier hv = HttpsURLConnection.getDefaultHostnameVerifier();
                Log.e("hey", "inside this");
                Log.e("HOST VERIFIER", hv.toString());
                Log.e("HOST NAME", hostname);
                return hv.verify("aviatesoftware.in", sslSession);
            }
        });
        return okHttpClient;
    } catch (Exception e) {
        Log.e("error while getting ","unsafeOkHttpClient "+e.toString());
    }
    return null;
}

public static <S> S createService(Class<S> serviceClass) {

    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(API_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(getUnsafeOkHttpClient())
            .build();

    return retrofit.create(serviceClass);
}


GetInterface obj  = createService(GetTaxInterface.class);
            Call<Abc> call = obj.getAbc("555555555555");
            call.enqueue(new Callback<Abc>() {



                @Override
                public void onResponse(Response<Abc> response, Retrofit retrofit) {
                    Log.e("Asynchronous response", response.toString());

                    Abc temp = response.body();
                    Log.e("tax object", temp.toString());
                    Toast.makeText(getApplicationContext(),"Retrofit Asynchonus Simple try successful",Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFailure(Throwable t) {
                    Log.e("on Failure", t.toString());
                    Toast.makeText(getApplicationContext(),"Retrofit Asynchonus Simple try failed",Toast.LENGTH_SHORT).show();
                }
            });
