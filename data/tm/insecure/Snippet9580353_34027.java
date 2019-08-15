  public Boolean PostData(String data) throws IOException {
  Boolean response = false;
  TrustManager[] trustAllCerts = new TrustManager[] {new X509TrustManager() {
    @Override
    public java.security.cert.X509Certificate[] getAcceptedIssuers() {
      return null;
    }

    @Override
    public void checkClientTrusted(java.security.cert.X509Certificate[] certs, String authType) {
    }

    @Override
    public void checkServerTrusted(java.security.cert.X509Certificate[] certs, String authType) {
    }
  }};

  // Install the all-trusting trust manager
  try {
    SSLContext sc = SSLContext.getInstance("TLS");
    sc.init(null, trustAllCerts, new java.security.SecureRandom());

    HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
  } catch (Exception e) {
  }


  HostnameVerifier hostnameVerifier =
      org.apache.http.conn.ssl.SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER;


  SchemeRegistry registry = new SchemeRegistry();
  SSLSocketFactory socketFactory = SSLSocketFactory.getSocketFactory();
  socketFactory.setHostnameVerifier((X509HostnameVerifier) hostnameVerifier);
  registry.register(new Scheme("https", socketFactory, 443));

  // Set verifier
  StringBuilder postDataBuilder = new StringBuilder();
  postDataBuilder.append("data").append("=").append(data);
  byte[] postData = postDataBuilder.toString().getBytes("UTF-8");

  URL postURL = new URL(url);

  HttpsURLConnection conn = (HttpsURLConnection) postURL.openConnection();
  conn.setDoOutput(true);
  conn.setUseCaches(false);
  HttpsURLConnection.setDefaultHostnameVerifier(new CustomizedHostnameVerifier());
  conn.setRequestMethod("POST");
  conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
  conn.setRequestProperty("Content-Length", Integer.toString(postData.length));

  // ------------------
  OutputStream out = conn.getOutputStream();
  // --------------------------
  out.write(postData);
  out.close();

  return response;
}

private static class CustomizedHostnameVerifier implements HostnameVerifier {
  @Override
  public boolean verify(String hostname, SSLSession session) {
    return true;
  }
}
