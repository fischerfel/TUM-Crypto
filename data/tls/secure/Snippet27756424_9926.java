KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory
        .getDefaultAlgorithm());
KeyStore keyStore = KeyStore.getInstance("pkcs12");
InputStream inputStream = new FileInputStream("YOUR.p12");

keyStore.load(inputStream, "Your password as char[]");
keyManagerFactory.init(keyStore, "Your password as char[]");

SSLContext sslContext = SSLContext.getInstance("TLSv1.2");
sslContext.init(keyManagerFactory.getKeyManagers(), null,new SecureRandom());
AsyncHttpClientConfig httpClientConfig = new AsyncHttpClientConfig.Builder().setSSLContext(sslContext).build();
AsyncHttpClient httpClient = new AsyncHttpClient(httpClientConfig);
