HttpClient httpClient = new DefaultHttpClient();
SSLSocketFactory sf = (SSLSocketFactory)httpClient.getConnectionManager()
    .getSchemeRegistry().getScheme("https").getSocketFactory();
sf.setHostnameVerifier(new AllowAllHostnameVerifier());
