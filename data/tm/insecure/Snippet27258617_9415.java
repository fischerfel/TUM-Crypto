class ClientHelper {
  public static HttpClientConnectionManager customConnectionManager() throws Exception {
  final SSLContext sslContext = SSLContext.getInstance("SSL");

sslContext.init(null, new TrustManager[]{new X509TrustManager() {
  @Override
  public void checkClientTrusted(X509Certificate[] x509Certificates, String s)
      throws CertificateException {
    System.out.println("========checkClientTrusted=========");
  }

  @Override
  public void checkServerTrusted(X509Certificate[] x509Certificates, String s)
      throws CertificateException {
    System.out.println("========checkServerTrusted==========");
  }

  @Override
  public X509Certificate[] getAcceptedIssuers() {
    return null;
  }
}}, new SecureRandom());

SSLConnectionSocketFactory
    sslConnectionSocketFactory =
    new SSLConnectionSocketFactory(sslContext);
Registry<ConnectionSocketFactory> registry = RegistryBuilder.<ConnectionSocketFactory>create()
    .register("https", sslConnectionSocketFactory)
    .register("http", PlainConnectionSocketFactory.getSocketFactory())
    .build();
PoolingHttpClientConnectionManager phcc = new PoolingHttpClientConnectionManager(registry);
return phcc;
}

public static Client createClient() {
  HttpClient apacheClient = null;
  try {
  apacheClient =
      HttpClientBuilder.create().setConnectionManager(customConnectionManager()).build();

} catch (Exception e) {
  e.printStackTrace();
}
Client client = new Client(new ApacheHttpClient4Handler(apacheClient,
                                                        new BasicCookieStore(),
                                                        true));
return client;
  }
  }
