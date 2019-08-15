public class HttpClient {
private static OkHttpClient.Builder builder;

public static synchronized OkHttpClient getOkHttpClient(){
    try {
    TrustManager[] trustManagers = new TrustManager[]{
            new X509TrustManager() {
                @Override
                public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {

                }

                @Override
                public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {

                }

                @Override
                public X509Certificate[] getAcceptedIssuers() {
                    return new X509Certificate[0];
                }
            }
    };

        SSLContext sslContext = SSLContext.getInstance("SSL");
        sslContext.init(null,trustManagers,new SecureRandom());
        SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        builder = new OkHttpClient().newBuilder()
                .sslSocketFactory(sslSocketFactory, (X509TrustManager) trustManagers[0])
                .addInterceptor(httpLoggingInterceptor)
                .hostnameVerifier(new HostnameVerifier() {
                    @Override
                    public boolean verify(String hostname, SSLSession session) {
                        return true;
                    }
                });

    } catch (NoSuchAlgorithmException e) {
        e.printStackTrace();
    } catch (KeyManagementException e) {
        e.printStackTrace();
    }

    return builder.build();
}
}
