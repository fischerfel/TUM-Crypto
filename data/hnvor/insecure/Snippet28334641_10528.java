//Open up connection 
httpsConnection = (HttpsURLConnection) new URL(url).openConnection();

httpsConnection.setHostnameVerifier(new HostnameVerifier() {
  @Override
  public boolean verify(String hostname, SSLSession session) {
   return true;
   }
 });
