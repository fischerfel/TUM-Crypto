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
    builder.setClient(new OkClient(getUnsafeOkHttpClient()));
    mRestAdapter = builder.build();

}

private static OkHttpClient getUnsafeOkHttpClient() {
    try {
        // Create a trust manager that does not validate certificate chains
        final TrustManager[] trustAllCerts = new TrustManager[]{
                new X509TrustManager() {
                    @Override
                    public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                    }

                    @Override
                    public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                    }

                    @Override
                    public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                        return null;
                    }
                }
        };

        // Install the all-trusting trust manager
        final SSLContext sslContext = SSLContext.getInstance("SSL");
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

public RaasService getService() {
    if (mRaasService == null) {
        mRaasService = mRestAdapter.create(RaasService.class);
    }
    return mRaasService;
}
