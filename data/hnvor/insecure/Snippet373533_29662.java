HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
conn.setHostnameVerifier(new HostnameVerifier() {
  public boolean verify(String hostname, SSLSession session) {
    // check hostname/session
    return true;
  }
});
conn.connect();
// read/write...
