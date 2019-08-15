 public String okHttpRequest(){

    try {
        final TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
            @Override
            public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {

            }

            @Override
            public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {

            }

            @Override
            public X509Certificate[] getAcceptedIssuers() {
                return null;
            }
        }
        };

        final SSLContext sslContext = SSLContext.getInstance("SSL");
        sslContext.init(null,trustAllCerts, new java.security.SecureRandom());
        final javax.net.ssl.SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.sslSocketFactory(sslSocketFactory);
        builder.hostnameVerifier(new HostnameVerifier() {
            @Override
            public boolean verify(String hostname, SSLSession session) {
                return true;
            }
        }).build();
        final OkHttpClient client = new OkHttpClient();

        HttpUrl url = HttpUrl.parse("https://myUrl.com/login").newBuilder()
                .addQueryParameter("username", "123456")
                .addQueryParameter("password", "123456")
                .build();
        Request request = new Request.Builder()
                .url(url)
                .build();


        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                Log.d("Request:", "Is NOT sent " + request.toString() + " METHOD: " + request.method());
                e.printStackTrace();
            }

            @Override
            public void onResponse(Response response) throws IOException {
                Log.d("Request:", "Is sent " + response.toString());
            }
        });
    }catch(Exception e){
        e.printStackTrace();
    }
    return "okHttp is Working!!! ";

}    
