  private InputStream connectHTTPS(String loginData, String url, String params) {
  TrustManager[] trustAllCerts =
  new TrustManager[]{
    new X509TrustManager(){
      public java.security.cert.X509Certificate[] getAcceptedIssuers() {
        return null;
      }
      public void checkClientTrusted(java.security.cert.X509Certificate[] certs, String authType){
      }
      public void checkServerTrusted(java.security.cert.X509Certificate[] certs, String authType) {
      }
      public boolean isClientTrusted(java.security.cert.X509Certificate[] certs) {
        return true;
      }

      public boolean isServerTrusted(java.security.cert.X509Certificate[] certs) {
        return true;
      }
  }};
  HostnameVerifier verifier = new HostnameVerifier() {
    public boolean verify(String string, SSLSession sSLSession) {
      return true;
    }
    public boolean verify(String string, String string2) {
      return true;
    }
  };
  SSLContext sc = null;
  try{
      sc = SSLContext.getInstance("SSL");
      sc.init(null, trustAllCerts, new java.security.SecureRandom());
      javax.net.ssl.HttpsURLConnection.setDefaultHostnameVerifier(verifier);
  }catch(Exception e){
      e.printStackTrace();
  }
  javax.net.ssl.HttpsURLConnection.setDefaultHostnameVerifier(verifier);
  URL serverURL = null;
  try{
      serverURL = new URL(null,url,new sun.net.www.protocol.https.Handler());
  }catch (Exception e){
       e.printStackTrace();
  }
  javax.net.ssl.HttpsURLConnection urlConn = null;
  try{
      urlConn = (javax.net.ssl.HttpsURLConnection)serverURL.openConnection();
      urlConn.setSSLSocketFactory(sc.getSocketFactory());
      urlConn.setRequestMethod("POST");
  }catch(Exception i){
    i.printStackTrace();
  }
  InputStream res = null;
  urlConn.setDoOutput(true);
  if(useLogin)
    urlConn.setRequestProperty("Authorization", "Basic " + loginData);
  urlConn.setUseCaches(false);
  urlConn.setAllowUserInteraction(true);
  urlConn.setRequestProperty("Pragma", "no-cache");
  urlConn.setRequestProperty("Content-type", "application/x-www-form-urlencoded");
  urlConn.setRequestProperty("Content-length", Integer.toString(params.length()));
  try{
      PrintStream out = new PrintStream(urlConn.getOutputStream());
      out.print(params);
      out.flush();
      out.close();
      res = urlConn.getInputStream();
  }catch(Exception o){
      o.printStackTrace();
  }
  return res;
  }
