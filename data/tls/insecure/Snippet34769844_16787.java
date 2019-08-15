@Override
protected Void doInBackground(Void... params) {
    login();
    return null;
}


private KeyStore getKeyStore() {
    KeyStore trusted;
    try {
        trusted = KeyStore.getInstance("BKS");
        InputStream in = activity.getBaseContext().getResources().openRawResource(R.raw.cert);
        try {
            trusted.load(in, "password".toCharArray());
        } finally {
            in.close();
        }
    } catch (Exception e) {
        throw new AssertionError(e);
    }

    return trusted;
}

public void login() {
    HttpsURLConnection urlConnection = null;
    try {
        //prepare request
        URL url = new URL("https://" + ip + "/api/login");

        urlConnection = (HttpsURLConnection) url.openConnection();

        TrustManagerFactory tmf = null;
        SSLContext context;
        try {
            tmf = TrustManagerFactory.getInstance("X509");
            tmf.init(getKeyStore());
            context = SSLContext.getInstance("TLS");
            context.init(null, tmf.getTrustManagers(), null);

            urlConnection.setSSLSocketFactory(context.getSocketFactory());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }  catch (KeyManagementException e) {
            e.printStackTrace();
        }

        urlConnection.setRequestMethod("POST");
        urlConnection.addRequestProperty("Content-Type", "application/json");

        //write request
        urlConnection.setDoOutput(true);
        urlConnection.setChunkedStreamingMode(0);

        //error at this line
        OutputStreamWriter out = new OutputStreamWriter(urlConnection.getOutputStream());

        String logginRequest = "{'login' : '" + login + "', 'password' : '" + password + "'}";
        out.write(logginRequest.toCharArray());
        out.close()

        //connect
        urlConnection.connect();

        //read
        BufferedInputStream in = new BufferedInputStream(urlConnection.getInputStream());
        printData(in);
        in.close();
    } catch (MalformedURLException e) {
        Log.e("ConcentratorExport", "MalformedURLException");
        e.printStackTrace();
    } catch (IOException e) {
        Log.e("ConcentratorExport", "IOException");
        e.printStackTrace();
    } finally {
        urlConnection.disconnect();
    }
}
