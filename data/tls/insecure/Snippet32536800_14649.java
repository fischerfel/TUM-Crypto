org.json.JSONObject json = new org.json.JSONObject();

    org.json.JSONObject root = new org.json.JSONObject();
    root.put("Version", "1");
    json.put("key", "test");
    root.put("message", json);
    System.out.println(root);

    String url = "https://example.com";
App obi = new App();
    obi.trustSelfSignedSSL();

    System.setProperty("javax.net.ssl.keyStore", "/Users/crohitk/Desktop/spring/keystore.jks");

   System.setProperty("javax.net.ssl.keyStorePassword", "password");
   System.setProperty("javax.net.ssl.trustStore","/Users/crohitk/Desktop/spring/ca-certs.jks");

   System.setProperty("javax.net.ssl.trustStorePassword","");


   KeyStore keyStore = null;
   KeyStore truststore = null;

        keyStore = KeyStore.getInstance(KeyStore.getDefaultType());

        keyStore.load(new FileInputStream("/Users/crohitk/Desktop/spring/keystore.jks"),
                "password".toCharArray());


    SSLConnectionSocketFactory socketFactory = null;

        socketFactory = new SSLConnectionSocketFactory(
                new SSLContextBuilder()

                        .loadKeyMaterial(keyStore, "password".toCharArray()).build());


    HttpClient httpClient = HttpClients.custom().setSSLSocketFactory(socketFactory).build();

    ClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory(
            httpClient);

    RestTemplate restTemplate = new RestTemplate(requestFactory);





  String result = restTemplate.postForObject(url, root , String.class);
    System.out.println(result);
    System.out.println( "Hello World!" );
}

    public static void trustSelfSignedSSL() {
        try {
            SSLContext ctx = SSLContext.getInstance("TLS");
            X509TrustManager tm = new X509TrustManager() {

                public void checkClientTrusted(X509Certificate[] xcs, String string) throws CertificateException {
                }

                public void checkServerTrusted(X509Certificate[] xcs, String string) throws CertificateException {
                }

                public X509Certificate[] getAcceptedIssuers() {
                    return null;
                }
            };
            ctx.init(null, new TrustManager[]{tm}, null);
            SSLContext.setDefault(ctx);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        }
