private HostnameVerifier getHostnameVerifier() {
  HostnameVerifier hv = new HostnameVerifier() {
    public boolean verify( String arg0, SSLSession arg1 ) { return true; }
  };
  return hv;
}

private SSLContext getSslContext() throws Exception {      
  private final SSLContext sslContext = SSLContext.getInstance( "SSL" );
  sslContext.init( null, new TrustManager[] { new X509TrustManager() {
        public X509Certificate[] getAcceptedIssuers() { return new X509Certificate[0]; }
        public void checkClientTrusted( X509Certificate[] certs, String authType ) {}
        public void checkServerTrusted( X509Certificate[] certs, String authType ) {}
      }
    }, new SecureRandom()
  );
  return sslContext;
}
