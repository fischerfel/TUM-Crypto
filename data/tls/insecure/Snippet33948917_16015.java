private OkHttpClient myOkHttpClient(){

    try{
        OkHttpClient okHttpClient = new OkHttpClient();
        okHttpClient.setReadTimeout(70 * 1000, TimeUnit.MILLISECONDS);
        okHttpClient.setConnectTimeout(70 * 1000, TimeUnit.MILLISECONDS);

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
                return null;
            }
        } };


        final SSLContext sslContext = SSLContext.getInstance("SSL");
        sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
        // Create an ssl socket factory with our all-trusting manager
        final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

        okHttpClient.setSslSocketFactory(sslSocketFactory);

        return okHttpClient;

    }catch (Exception e){
        return null;
    }
}
