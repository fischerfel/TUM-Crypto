import okhttp3.Call;
import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.Headers;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

......

OkHttpClient.Builder clientBuilder = client.newBuilder().readTimeout(LOGIN_TIMEOUT_SEC, TimeUnit.SECONDS);

            boolean allowUntrusted = true;

            if (  allowUntrusted) {
                Log.w(TAG,"**** Allow untrusted SSL connection ****");
                final TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
                    @Override
                    public X509Certificate[] getAcceptedIssuers() {
                        X509Certificate[] cArrr = new X509Certificate[0];
                        return cArrr;
                    }

                    @Override
                    public void checkServerTrusted(final X509Certificate[] chain,
                                                   final String authType) throws CertificateException {
                    }

                    @Override
                    public void checkClientTrusted(final X509Certificate[] chain,
                                                   final String authType) throws CertificateException {
                    }
                }};

                SSLContext sslContext = SSLContext.getInstance("SSL");

                sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
                clientBuilder.sslSocketFactory(sslContext.getSocketFactory());

                HostnameVerifier hostnameVerifier = new HostnameVerifier() {
                    @Override
                    public boolean verify(String hostname, SSLSession session) {
                        Log.d(TAG, "Trust Host :" + hostname);
                        return true;
                    }
                };
                clientBuilder.hostnameVerifier( hostnameVerifier);
            }

            final Call call = clientBuilder.build().newCall(request);
