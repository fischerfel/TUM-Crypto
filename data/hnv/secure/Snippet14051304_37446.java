// always verify the host - dont check for certificate
final static HostnameVerifier DO_NOT_VERIFY = new HostnameVerifier() {
    public boolean verify(String hostname, SSLSession session) {
        return true;
    }
};

/**
 * Trust every server - dont check for any certificate
 */
private static void trustAllHosts() {
    // Create a trust manager that does not validate certificate chains
    TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
            return new java.security.cert.X509Certificate[] {};
        }

        public void checkClientTrusted(X509Certificate[] chain,
                String authType) throws CertificateException {
        }

        public void checkServerTrusted(X509Certificate[] chain,
                String authType) throws CertificateException {
        }
    } };

    // Install the all-trusting trust manager
    try {
        SSLContext sc = SSLContext.getInstance("TLS");
        sc.init(null, trustAllCerts, new java.security.SecureRandom());
        HttpsURLConnection
                .setDefaultSSLSocketFactory(sc.getSocketFactory());
    } catch (Exception e) {
        e.printStackTrace();
    }
}

private void makeRequest() {
    URL url = null;
    HttpsURLConnection urlConnection = null;
    try {
        url = new URL("https://wbapi.cloudapp.net:443/api/User/LocalLogin");
    } catch (MalformedURLException e2) {
        // TODO Auto-generated catch block
        e2.printStackTrace();
    }
    StringBuilder sb = new StringBuilder();

    try {
        trustAllHosts();
        urlConnection = (HttpsURLConnection) url.openConnection();
        urlConnection.setHostnameVerifier(DO_NOT_VERIFY);
        urlConnection.setDoOutput(true);
        urlConnection
                .setRequestProperty("Content-Type", "application/json");
        urlConnection.setFixedLengthStreamingMode(urlConnection
                .getContentLength());
    } catch (IOException e2) {
        // TODO Auto-generated catch block
        e2.printStackTrace();
    }

    try {
        urlConnection.connect();
    } catch (IOException e1) {
        // TODO Auto-generated catch block
        e1.printStackTrace();
    }
    JSONObject jsonParam = new JSONObject();
    try {
        jsonParam.put("Name", "dog");
        jsonParam.put("Password", "123");
        Log.v("Length", "" + urlConnection.getContentLength());
        int HttpResult = urlConnection.getResponseCode();
        Toast.makeText(LoginActivity.this, "Response" + HttpResult,
                Toast.LENGTH_LONG).show();
        if (HttpResult == HttpsURLConnection.HTTP_OK) {
            System.out.println("ok");
            Log.v("Hi", "" + "Trex");
            BufferedReader br = new BufferedReader(new InputStreamReader(
                    urlConnection.getInputStream(), "utf-8"));
            String line = null;
            while ((line = br.readLine()) != null) {
                sb.append(line + "\n");
            }
            br.close();

            System.out.println("" + sb.toString());
            Toast.makeText(LoginActivity.this, "" + sb.toString(),
                    Toast.LENGTH_LONG).show();

        } else {
            System.out.println("Here" + urlConnection.getResponseMessage());
        }
    } catch (MalformedURLException e) {

        e.printStackTrace();
    } catch (IOException e) {

        e.printStackTrace();
    } catch (JSONException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    }
}
