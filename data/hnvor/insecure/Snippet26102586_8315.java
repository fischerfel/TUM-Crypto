sslSocketFactory.setHostnameVerifier(new HostnameVerifier() {
    public boolean verify(String hostname, SSLSession session) {
      return true;
    }
};
