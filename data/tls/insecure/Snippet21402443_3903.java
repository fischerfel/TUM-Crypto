public String doRequest(String url, HashMap<Object, Object> data,
        Method method, String token) throws Exception {

    InputStream certificateInputStream = null;
    if (SomeApplication.PRODUCTION) {
        certificateInputStream = SomeApplication.context
                .getResources().openRawResource(R.raw.production_cert);
        LogUtils.log("using production SSL certificate");
    } else {
        certificateInputStream = SomeApplication.context
                .getResources().openRawResource(R.raw.staging_cert);
        LogUtils.log("using staging SSL certificate");
    }

    KeyStore trustStore = KeyStore.getInstance("BKS");
    trustStore.load(certificateInputStream,
            "xxxxxxxxxxxx".toCharArray());
    certificateInputStream.close();

    TrustManagerFactory tmf = TrustManagerFactory.getInstance("X509");
    tmf.init(trustStore);
    SSLContext context = SSLContext.getInstance("TLS");
            // this log returns 1 for trustmanagers. 
    LogUtils.log("tmf get trustmanagers: " + tmf.getTrustManagers().length);
    context.init(null, tmf.getTrustManagers(), null);
URL request = new URL(url);
        HttpsURLConnection urlConnection = (HttpsURLConnection) request
                .openConnection();

        urlConnection.setHostnameVerifier(new StrictHostnameVerifier());
        urlConnection.setSSLSocketFactory(context.getSocketFactory());
        urlConnection.setConnectTimeout(15000);
        if (method != Method.GET)
            urlConnection.setDoOutput(true);
        urlConnection.setDoInput(true);
        urlConnection.setRequestProperty("Content-Type", "application/json");
        urlConnection.setRequestProperty("Accept", "application/json");
        if (token != null) {
            urlConnection.setRequestProperty("Authorization", "Token " + token);
        }
        urlConnection.setRequestMethod(method.toString());
        urlConnection.connect();
