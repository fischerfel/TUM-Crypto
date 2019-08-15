SSLContext ctx = SSLContext.getInstance("TLS");
ctx.init(null, new TrustManager[] {
  new X509TrustManager() {
    public void checkClientTrusted(X509Certificate[] chain, String authType) {}
    public void checkServerTrusted(X509Certificate[] chain, String authType) {}
    public X509Certificate[] getAcceptedIssuers() { return new X509Certificate[]{}; }
  }
}, null);
HttpsURLConnection.setDefaultSSLSocketFactory(ctx.getSocketFactory());
