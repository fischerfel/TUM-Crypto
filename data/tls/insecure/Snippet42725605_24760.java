    // HTTP POST request
private void sendPost(String token) throws Exception {

    try {

        token = "blablabla";

        TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                return new X509Certificate[0];
            }

            public void checkClientTrusted(java.security.cert.X509Certificate[] certs, String authType) {
            }

            public void checkServerTrusted(java.security.cert.X509Certificate[] certs, String authType) {
            }
        } };

        SSLContext sc = SSLContext.getInstance("SSL");
        sc.init(null, trustAllCerts, new java.security.SecureRandom());
        HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

        CloseableHttpClient httpClient = HttpClientBuilder.create().build();

        SSLConnectionSocketFactory f = new SSLConnectionSocketFactory(sc, new String[] { "TLSv1.2" }, null,
                org.apache.http.conn.ssl.SSLConnectionSocketFactory.BROWSER_COMPATIBLE_HOSTNAME_VERIFIER);

        httpClient = HttpClients.custom().setSSLSocketFactory(f).build();

        HttpPost postRequest = new HttpPost("https://localhost:9444/rest/service/sample/restService");

        postRequest.addHeader("Content-Type", "application/x-www-form-urlencoded");

        List<NameValuePair> formparams = new ArrayList<NameValuePair>();
        formparams.add(new BasicNameValuePair("client_id", clientId));
        formparams.add(new BasicNameValuePair("client_secret", clientSecret));
        formparams.add(new BasicNameValuePair("token", token));

        UrlEncodedFormEntity entity = new UrlEncodedFormEntity(formparams, "utf-8");
        postRequest.setEntity(entity);

        postRequest.setHeader("Authorization", "Bearer " + token + "");

        HttpResponse response = httpClient.execute(postRequest, new BasicHttpContext());

        int statusCode = response.getStatusLine().getStatusCode();

        logger.info("HTTP status code : " + statusCode);

    } catch (Exception e) {
        logger.error(e.getMessage());
    }
}
