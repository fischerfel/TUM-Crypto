public static RestAdapter createAdapter(Context context) {
  // loading CAs from an InputStream
  CertificateFactory cf = CertificateFactory.getInstance("X.509");
  InputStream cert = context.getResources().openRawResource(R.raw.my_cert);
  Certificate ca;
  try {
    ca = cf.generateCertificate(cert);
  } finally { cert.close(); }

  // creating a KeyStore containing our trusted CAs
  String keyStoreType = KeyStore.getDefaultType();
  KeyStore keyStore = KeyStore.getInstance(keyStoreType);
  keyStore.load(null, null);
  keyStore.setCertificateEntry("ca", ca);

  // creating a TrustManager that trusts the CAs in our KeyStore
  String tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
  TrustManagerFactory tmf = TrustManagerFactory.getInstance(tmfAlgorithm);
  tmf.init(keyStore);

  // creating an SSLSocketFactory that uses our TrustManager
  SSLContext sslContext = SSLContext.getInstance("TLS");
  sslContext.init(null, tmf.getTrustManagers(), null);

  // creating an OkHttpClient that uses our SSLSocketFactory
  OkHttpClient okHttpClient = new OkHttpClient();
  okHttpClient.setSslSocketFactory(sslContext.getSocketFactory());

  // creating a RestAdapter that uses this custom client
  return new RestAdapter.Builder()
              .setEndpoint(UrlRepository.API_BASE)
              .setClient(new OkClient(okHttpClient))
              .build();
}
