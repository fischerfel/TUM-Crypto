public class NetworkCallSecure extends AsyncTask<String, Void, String> {

ResponseListener responseListener;
Activity activity;
ResultCodes code;

public NetworkCallSecure(Activity activity, ResponseListener responseListener, ResultCodes code) {
    this.responseListener = responseListener;
    this.activity = activity;
    this.code = code;
}

@Override
protected String doInBackground(String... params) {

    try{

        System.setProperty("http.keepAlive", "false");
        HttpsURLConnection .setDefaultHostnameVerifier(new HostnameVerifier() {

                    public boolean verify(String hostname,
                                          SSLSession session) {
                        Log.d("HTTPS",hostname+":"+session);
                        return true;
                    }
                });

        char[] passwKey = "mypass".toCharArray();
        KeyStore ks = KeyStore.getInstance("BKS");
        InputStream in = activity.getResources().openRawResource(
                R.raw.client);
        InputStream is = activity.getResources().openRawResource(
                R.raw.client);
        ks.load(in, passwKey);
        KeyManagerFactory kmf = KeyManagerFactory.getInstance("X509");
        kmf.init(ks, passwKey);

        SSLContext context = SSLContext.getInstance("TLS");
        context.init(kmf.getKeyManagers(),
                new X509TrustManager[] { new MyX509TrustManager(is,
                        passwKey) }, new SecureRandom());
        HttpsURLConnection.setDefaultSSLSocketFactory(context
                .getSocketFactory());

        URL url = new URL(params[0]);

        HttpsURLConnection connection = (HttpsURLConnection) url
                .openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setRequestProperty("Content-Length", "" + Integer.toString(params[1].getBytes().length));
        connection.setDoOutput(true);

        byte[] outputInBytes = params[1].getBytes("UTF-8");
        OutputStream os = connection.getOutputStream();
        os.write( outputInBytes );
        os.close();

        BufferedReader bin = new BufferedReader(new InputStreamReader(
                connection.getInputStream()));

        StringBuffer sb = new StringBuffer();
        String line;
        while ((line = bin.readLine()) != null) {
            sb.append(line);
        }
        in.close();
        is.close();
        return sb.toString();
    } catch (Exception e) { // should never happen
        e.printStackTrace();
        Log.d("Err", e.toString());
    }
    return "no result";
}

@Override
protected void onPostExecute(String result) {
    responseListener.getResponse(result,code);
}
}
