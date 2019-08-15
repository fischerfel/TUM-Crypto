public String sendPost(final String request, final String postData) throws ClientProtocolException, IOException, NoSuchAlgorithmException, KeyManagementException  {
    String result = null;
    SSLContext sslContext = SSLContext.getInstance("SSL");

    // set up a TrustManager that trusts everything
    sslContext.init(null, new TrustManager[] { new X509TrustManager() {
                public X509Certificate[] getAcceptedIssuers() {
                        System.out.println("getAcceptedIssuers =============");
                        return null;
                }

                public void checkClientTrusted(X509Certificate[] certs,
                                String authType) {
                        System.out.println("checkClientTrusted =============");
                }

                public void checkServerTrusted(X509Certificate[] certs,
                                String authType) {
                        System.out.println("checkServerTrusted =============");
                }
    } }, new SecureRandom());

    CloseableHttpClient httpclient = HttpClients.custom().setSSLSocketFactory(new SSLSocketFactory(sslContext)).build();
    HttpPost httpPost = new HttpPost(request);
    ByteArrayEntity postDataEntity = new ByteArrayEntity(postData.getBytes());
    httpPost.setEntity(postDataEntity);
    CloseableHttpResponse response = httpclient.execute(httpPost);
    try {
        HttpEntity entity = response.getEntity();
        result = EntityUtils.toString(entity);
        EntityUtils.consume(entity);
    } finally {
        response.close();
    }
    return result;

}
