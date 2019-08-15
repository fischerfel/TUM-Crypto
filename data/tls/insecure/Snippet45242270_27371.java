final TrustManager[] trustAllCerts = new TrustManager[] 
{
  new javax.net.ssl.X509TrustManager() {
  @Override
  public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException { }

 @Override
 public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {}

 @Override
 public java.security.cert.X509Certificate[] getAcceptedIssuers() { }
};

//and then
 sSslContext = SSLContext.getInstance("TLS");
 sSslContext.init(null, trustAllCerts, null);
