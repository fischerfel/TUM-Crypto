URL url = new URL(urlStr);

URLConnection conn = url.openConnection();

if (conn instanceof HttpsURLConnection) {

  HttpsURLConnection conn1 = (HttpsURLConnection) url.openConnection();

  conn1.setHostnameVerifier(new HostnameVerifier() {
    public boolean verify(String hostname, SSLSession session) {
      return true;
    }
  });

  reply.load(conn1.getInputStream());
