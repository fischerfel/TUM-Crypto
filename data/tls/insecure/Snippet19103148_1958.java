    KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory
            .getDefaultAlgorithm());
    KeyStore clientCert = KeyStore.getInstance("pkcs12");
    InputStream is = new FileInputStream("/conf/brazo.p12");

    clientCert.load(is, "brazo".toCharArray());
    kmf.init(clientCert, "brazo".toCharArray());

    SSLContext sc = SSLContext.getInstance("TLSv1");
    sc.init(kmf.getKeyManagers(), null,
            new SecureRandom());
    AsyncHttpClientConfig config = new AsyncHttpClientConfig.Builder().setSSLContext(sc).build();
    AsyncHttpClient client = new AsyncHttpClient(config);

    String url = "https://your-url";
    Request req = new RequestBuilder().setUrl(url).setMethod("GET").build();
    client.executeRequest(req);
