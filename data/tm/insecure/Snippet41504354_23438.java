public static synchronized OkHttpClient getAllAcceptingOkHttpClient() {
        if (sOkHttpClient == null) {
            SSLSocketFactory factory = getAllAcceptingSSLSocketFactory();
            OkHttpClient.Builder builder = new OkHttpClient.Builder();
            builder.cookieJar(new JavaNetCookieJar(AadharApplication.cookieManager));
            if (factory != null) {
                builder.sslSocketFactory(factory);
            }
            builder.hostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            });
            sOkHttpClient = builder.build();
        }
        return sOkHttpClient;
    }


private static SSLSocketFactory getAllAcceptingSSLSocketFactory() {
        try {
            final SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, sTrustAllCerts, new java.security.SecureRandom());
            return sslContext.getSocketFactory();
        } catch (KeyManagementException | NoSuchAlgorithmException e) {
            Log.d(TAG, "", e);
            return null;
        }
    }

    private static final TrustManager[] sTrustAllCerts = new TrustManager[]{
            new X509TrustManager() {
                @Override
                public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                }

                @Override
                public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                }

                @Override
                public X509Certificate[] getAcceptedIssuers() {
                    return new X509Certificate[]{};
                }
            }
    };
