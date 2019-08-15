public JSONObject makeHttpRequest(String url, String method,
        List<NameValuePair> params) throws NoSuchAlgorithmException,
        CertificateException, NotFoundException, KeyStoreException,
        KeyManagementException {

    // Making HTTP request
    try {

        // check for request method
        if (method == "POST") {

            // request method is POST
            // defaultHttpClient

            // Create a trust manager that does not validate certificate chains
            TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
                public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                    return null;
                }

                public void checkClientTrusted(
                        java.security.cert.X509Certificate[] certs,
                        String authType) {
                }

                public void checkServerTrusted(
                        java.security.cert.X509Certificate[] certs,
                        String authType) {
                }
            } };

            // Install the all-trusting trust manager
            try {
                SSLContext sc = SSLContext.getInstance("SSL");
                sc.init(null, trustAllCerts,
                        new java.security.SecureRandom());
                HttpsURLConnection.setDefaultSSLSocketFactory(sc
                        .getSocketFactory());
            } catch (Exception e) {
            }

            // Now you can access an https URL without having the certificate in the truststore

            HttpClient client = new DefaultHttpClient();
            client = this.sslClient(client);
            HttpPost httpPost = new HttpPost(url);
            httpPost.setEntity(new UrlEncodedFormEntity(params));

            // Log.v(TAG, EntityUtils.toString(result.getEntity()));

            HttpResponse httpResponse = client.execute(httpPost);
            // Log.v("httpresponsetag:", EntityUtils.toString(httpResponse
            // .getEntity()));
            HttpEntity httpEntity = httpResponse.getEntity();
            is = httpEntity.getContent();

        }
