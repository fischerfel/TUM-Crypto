public CloseableHttpClient getHttpClient() {
        try{
            SSLContext context = null;
            TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
                public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                    return null;
                }

                public void checkClientTrusted(java.security.cert.X509Certificate[] certs, String authType) {
                }

                public void checkServerTrusted(java.security.cert.X509Certificate[] certs, String authType) {
                }
            } };

            try {
                context = SSLContext.getInstance("SSL");
            } catch (NoSuchAlgorithmException e1) {
                e1.printStackTrace();
            }
            try {
                context.init(null, trustAllCerts, new java.security.SecureRandom());
            } catch (KeyManagementException e1) {
                e1.printStackTrace();
            }

            SSLConnectionSocketFactory sslConnectionFactory = new SSLConnectionSocketFactory(context, SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
            Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory> create()
                      .register("https", sslConnectionFactory)
                      .register("http", new PlainConnectionSocketFactory()).build();

            PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager(socketFactoryRegistry);
             // Increase max total connection to 200
             cm.setMaxTotal(20);
             // Increase default max connection per route to 20
             cm.setDefaultMaxPerRoute(10);
             RequestConfig defaultRequestConfig = RequestConfig.custom()
                        .setSocketTimeout(5000)
                        .setConnectTimeout(5000)
                        .setConnectionRequestTimeout(5000)
                        .build();
             CloseableHttpClient httpClient = HttpClients.custom().setConnectionManager(cm).setConnectionManagerShared(true)
                     .setDefaultRequestConfig(defaultRequestConfig).build();
            return httpClient;
        }catch(Exception ce){

        }
        return null;
    }
