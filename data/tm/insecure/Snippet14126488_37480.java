  SSLContext sslContext = SSLContext.getInstance("TLS");
  TrustManager tm = new X509TrustManager() {
    public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
    }

    public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
    }

    public X509Certificate[] getAcceptedIssuers() {
      return null;
    }
  };
  sslContext.init(null, new TrustManager[] { tm }, null);
  FTPSClient ftpsClient = new FTPSClient(sslContext);
