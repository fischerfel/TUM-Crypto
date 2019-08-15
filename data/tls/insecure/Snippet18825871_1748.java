    URL url;
    SSLContext sslContext = null;
    KeyStore keyStore = KeyStore.getInstance("BKS");    
    InputStream input = SportsApplication.getContext().getResources().openRawResource(R.raw.mykeystore);
    keyStore.load(input, "mypass".toCharArray());
    KeyManagerFactory kmf = KeyManagerFactory.getInstance("X509");
    kmf.init(keyStore, "mypass".toCharArray());
    KeyManager[] keyManagers = kmf.getKeyManagers();
    sslContext = SSLContext.getInstance("TLS");
    sslContext.init(keyManagers, null, null);

    HttpsURLConnection connection = null;
    String urlParameters = mJObject.toString();
    try {
        Log.w("client", mUrl);
        Log.v("client", "request: "+urlParameters);
        // Create connection
        mUrl = mUrl.replaceFirst("http", "https");
        url = new URL(mUrl);
        connection = (HttpsURLConnection) url.openConnection();
        try{

            connection.setSSLSocketFactory(sslContext.getSocketFactory());
        }
        catch (Exception e1) {
            e1.printStackTrace();
            int tx =0;
        }
        try{
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-type", "application/json");
        connection.setRequestProperty("Accept", "application/json");
        connection.setReadTimeout(dataTimeout);
        connection.setConnectTimeout(com.inmobly.buckeyes.client.Constants.DEFAULT_CONN_TIMEOUT);

        connection.setUseCaches(false);
        connection.setDoInput(true);
        connection.setDoOutput(true);
        }
        catch (Exception e1) {
            e1.printStackTrace();
            int tx =0;
        }
        // Send request
        DataOutputStream wr = null;
        try{
         wr = new DataOutputStream(connection.getOutputStream());
        }
        catch (Exception e1) {
            e1.printStackTrace();
            int tx =0;
        }
