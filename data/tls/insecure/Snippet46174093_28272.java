    public class MainActivity extends AppCompatActivity {


    public static final String BASE_URL = "abc.com/abc_cc/cc/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
.client(getUnsafeOkHttpClient())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        Endpoints endpoints= retrofit.create(Endpoints.class);
        endpoints.newuser("{\"full_name\":\"sss\",\"states_id\":\"20\",\"mobile\":\"9876543210\",\"password\":\"******\",\"accept_terms\":true,\"Userid\":\"0\",\"refer\":\"\",\"ip-address\":\"1.2.3.4\",\"device_type\":\"samsung J5\",\"os-version\":\"5.0.1\",\"client\":\"app\",\"secret_key\":\"44\"}")
                .enqueue(new retrofit2.Callback<Items>() {
                    @Override
                    public void onResponse(retrofit2.Call<Items> call, retrofit2.Response<Items> response) {
                        System.out.println("onResponse : "+response.message());
                        System.out.println("onResponse : "+response.body());
                        System.out.println("onResponse : "+response.code());
                        System.out.println("onResponse : "+response.errorBody());
                        System.out.println("onResponse : "+response.isSuccessful());
                        System.out.println("onResponse : "+response.raw());
                        System.out.println("onResponse : "+response);
                    }

                    @Override
                    public void onFailure(retrofit2.Call<Items> call, Throwable t) {
                        System.out.println("onFailure"+call);

                    }
                });


    }

    public static OkHttpClient getUnsafeOkHttpClient() {

        try {
            // Create a trust manager that does not validate certificate chains
            final TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
                @Override
                public void checkClientTrusted(
                        java.security.cert.X509Certificate[] chain,
                        String authType) throws CertificateException {
                }

                @Override
                public void checkServerTrusted(
                        java.security.cert.X509Certificate[] chain,
                        String authType) throws CertificateException {
                }

                @Override
                public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                    return new java.security.cert.X509Certificate[0];
                }
            } };

            // Install the all-trusting trust manager
            final SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, trustAllCerts,
                    new java.security.SecureRandom());
            // Create an ssl socket factory with our all-trusting manager
            final SSLSocketFactory sslSocketFactory = sslContext
                    .getSocketFactory();

            OkHttpClient okHttpClient = new OkHttpClient();
            okHttpClient = okHttpClient.newBuilder()
                    .sslSocketFactory(sslSocketFactory)
                    .hostnameVerifier(org.apache.http.conn.ssl.SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER).build();

            return okHttpClient;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

}
