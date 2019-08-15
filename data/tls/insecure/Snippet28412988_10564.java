public Client buildUnsecureRestClient() throws GeneralSecurityException {
  SSLContext context = SSLContext.getInstance("TLSv1");
  TrustManager[] trustManagerArray = {
      ...
  };
  context.init(myKeyManagers, trustManagerArray, new SecureRandom());
  return ClientBuilder.newBuilder()
    .hostnameVerifier(myhostnameverifier)
    .sslContext(context)
    .build();
