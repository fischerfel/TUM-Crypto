String url = "https://path/to/url/service";
HttpClient client = new HttpClient();
PostMethod method = new PostMethod(url);

// Test whether to ignore cert errors
if (ignoreCertErrors){
  TrustManager[] trustAllCerts = new TrustManager[]{
    new X509TrustManager(){
      public X509Certificate[] getAcceptedIssuers(){ return null; }
      public void checkClientTrusted(X509Certificate[] certs, String authType) {}
      public void checkServerTrusted(X509Certificate[] certs, String authType) {}
    }
  };

  try {
    SSLContext sslContext = SSLContext.getInstance("SSL");
    sslContext.init(null, trustAllCerts, new SecureRandom());
    HttpsURLConnection.setDefaultSSLSocketFactory(sslContext.getSocketFactory());
  } catch (Exception e){
    e.printStackTrace();
  }
}

try {

  // Execute the method (Post) and set the results to the responseBodyAsString()
  int statusCode = client.executeMethod(method);
  resultsBody = method.getResponseBodyAsString();

} catch (HttpException e){
  e.printStackTrace();
} catch (IOException e){
  e.printStackTrace();
} finally {
  method.releaseConnection();
}
