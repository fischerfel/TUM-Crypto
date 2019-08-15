public String fetchFromUrl(String urlString) throws IOException {
  URL url = new URL(urlString);
  URLConnection conn = url.openConnection();

  if (conn instanceof HttpsURLConnection && shouldSubstituteCert(url)) {
    HttpsURLConnection sslConn = (HttpsURLConnection) conn;
    try {
      SSLContext context = SSLContext.getInstance("SSL");
      context.init(null, new TrustManager[] {new MyTrustManager()}, null);
      sslConn.setSSLSocketFactory(context.getSocketFactory());
    } catch (Exception e) {
      e.printStackTrace();
      throw new IOException("Error creating custom keystore", e);
    }
  }

  return readAll(conn.getInputStream());
}

private static class MyTrustManager implements X509TrustManager {
  private final X509TrustManager trustManager;
  public MyTrustManager() throws 
      KeyStoreException, NoSuchAlgorithmException, 
      CertificateException, IOException {

    // Load a KeyStore with only our certificate
    KeyStore store = KeyStore.getInstance(KeyStore.getDefaultType());
    store.load(null, null);
    Certificate cert = loadPemCert();
    store.setCertificateEntry("me.com", cert);

    // create a TrustManager using our KeyStore
    TrustManagerFactory factory = TrustManagerFactory.getInstance(
        TrustManagerFactory.getDefaultAlgorithm());
    factory.init(store);
    this.trustManager = getX509TrustManager(factory.getTrustManagers());
  }

  public void checkClientTrusted(X509Certificate[] chain, String authType)
      throws CertificateException {
    trustManager.checkClientTrusted(chain, authType);
  }

  public void checkServerTrusted(X509Certificate[] chain, String authType)
      throws CertificateException {
    trustManager.checkServerTrusted(chain, authType);
  }

  public X509Certificate[] getAcceptedIssuers() {
    return trustManager.getAcceptedIssuers();
  }

  private static X509TrustManager getX509TrustManager(TrustManager[] managers) {
    for (TrustManager tm : managers) {
      if (tm instanceof X509TrustManager) {
        return (X509TrustManager) tm;
      }
    }
    return null;
  }

  private Certificate loadPemCert() 
      throws CertificateException, IOException {
    InputStream stream = 
      this.getClass().getClassLoader().getResourceAsStream("cert.pem");

    CertificateFactory factory = CertificateFactory.getInstance("X.509");
    return factory.generateCertificate(stream);
  }
}
