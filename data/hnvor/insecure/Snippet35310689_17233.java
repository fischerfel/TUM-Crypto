public class RestModule {
     private RestAdapter mRestAdapter;
     private RaasService mRaasService;
     private String mAccessToken;

     public RestModule(final Context context, final String endPoint)
     {
          init(context, endPoint);
     }
     public RestModule(final Context context, final String endPoint, final String accessToken) {
          mAccessToken = accessToken;
          init(context, endPoint);
     }
     public void init(final Context context, final String endPoint) {
          final MyPreferences preference = MyPreferences.getInstance();
          final RestAdapter.Builder builder = new RestAdapter.Builder().setLogLevel(RestAdapter.LogLevel.FULL)
                  .setRequestInterceptor(new RequestInterceptor() {
                      @Override
                      public void intercept(RequestFacade requestFacade) {
                          if (mAccessToken == null) {
                              mAccessToken = preference.getCurrentAccountAccessToken();
                          }
                          requestFacade.addHeader("secretToken", mAccessToken);
                          requestFacade.addHeader("Content-Type", "application/json;charset=UTF-8");
                      }
                  })
                  .setEndpoint(endPoint);
          builder.setClient(new OkClient(getPinnedOkHttpClient(context)));
          mRestAdapter = builder.build();

     }

     private static OkHttpClient getPinnedOkHttpClient(Context context) {
          try {
              final SSLContext sslContext = getSslContext(context);
              sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
              // Create an ssl socket factory with our all-trusting manager
              final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();
              OkHttpClient okHttpClient = new OkHttpClient();
              okHttpClient.setSslSocketFactory(sslSocketFactory);
              okHttpClient.setHostnameVerifier(new HostnameVerifier() {
                  @Override
                  public boolean verify(String hostname, SSLSession session) {
                      return true;
                  }
              });
              okHttpClient.setConnectTimeout(30, TimeUnit.SECONDS);
              okHttpClient.setReadTimeout(30, TimeUnit.SECONDS);
              return okHttpClient;
          } catch (Exception e) {
              throw new RuntimeException(e);
          }
     }

     private SSLContext getSslContext(Context context) throws Exception {
         KeyStore trustStore = loadTrustStore(context);
         String algotithmName = TrustManagerFactory.getDefaultAlgorithm();
         TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(algotithmName);
         trustManagerFactory.init(trustStore);
         SSLContext sslContext = SSLContext.getInstance("TLS");
         sslContext.init(null, trustManagerFactory.getTrustManagers(), null);
         return sslContext;
     }

     private KeyStore loadTrustStore(Context context) throws Exception {
         KeyStore trustStore = KeyStore.getInstance("BKS");
         InputStream trustStoreStream = context.getResources().getAssets().open("trust.bks");
         trustStore.load(trustStoreStream, "password".toCharArray());
         return trustStore;
     }

     public RaasService getService() {
          if (mRaasService == null) {
              mRaasService = mRestAdapter.create(RaasService.class);
          }
          return mRaasService;
     }
}
