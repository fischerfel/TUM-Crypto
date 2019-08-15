  try {
    URL url = new URL("yourURL");

    URLConnection conn = url.openConnection();

    if (conn instanceof HttpsURLConnection) {
      // Try again as HTTPS
      HttpsURLConnection conn1 = (HttpsURLConnection) url.openConnection();
      conn1.setHostnameVerifier(new HostnameVerifier() {
        @Override
        public boolean verify(String hostname, SSLSession session) {
          return true;
        }
      });

      conn1.getResponseCode();
    }
  }
  catch (MalformedURLException e) {

  }
  catch (IOException e) {

  }
