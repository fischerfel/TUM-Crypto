  final TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {

    public X509Certificate[] getAcceptedIssuers() {
      return new X509Certificate[0];
    }

    public void checkClientTrusted(X509Certificate[] chain, String authType) {
      // Intentionally left blank
    }

    public void checkServerTrusted(X509Certificate[] chain, String authType) {
      // Intentionally left blank
    }
  } };
