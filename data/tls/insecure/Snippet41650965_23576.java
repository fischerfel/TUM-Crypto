@Provides
public OkHttpClient provideContactClient(){
  HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
  ConnectionSpec spec = new ConnectionSpec.Builder(ConnectionSpec.MODERN_TLS)
      .tlsVersions(TlsVersion.TLS_1_2)
      .cipherSuites(CipherSuite.TLS_RSA_WITH_DES_CBC_SHA,
          CipherSuite.TLS_RSA_WITH_AES_128_GCM_SHA256,
          CipherSuite.TLS_DHE_RSA_WITH_AES_128_GCM_SHA256)
      .build();
  interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
  SSLSocketFactory sslSocketFactory = null;
  try {
    SSLContext sslContext = SSLContext.getInstance("TLS");
    sslContext.init(null, null, null);
    sslSocketFactory = sslContext.getSocketFactory();
  }catch (GeneralSecurityException e){
    e.printStackTrace();
  }
  return new OkHttpClient.Builder()
      .addInterceptor(interceptor)
      .connectionSpecs(Collections.singletonList(spec))
      .sslSocketFactory(sslSocketFactory)
      .authenticator(new Authenticator() {
        @Override
        public Request authenticate(Route route, Response response) throws IOException {
          if(responseCount(response) >= 5){
            return null;
          }
          String credential = Credentials.basic("user", "pass");
          return response.request().newBuilder().header("Authorization", credential).build();
        }
      })
      .build();
}
