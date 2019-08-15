public ApiClient rebuildHttpClient() {
    // Add the JSON serialization support to Jersey
    JacksonJsonProvider jsonProvider = new JacksonJsonProvider(objectMapper);

    TrustManager[] byPassTrustManagers = new TrustManager[] { new X509TrustManager() {

      public X509Certificate[] getAcceptedIssuers() {
        return new X509Certificate[0];
      }

      public void checkClientTrusted(X509Certificate[] chain, String authType) {  }

      public void checkServerTrusted(X509Certificate[] chain, String authType) {   }

    } };


    HostnameVerifier hostnameVerifier = HttpsURLConnection.getDefaultHostnameVerifier();

    ClientConfig config = new DefaultClientConfig();

    SSLContext ctx = null;
    try {
      ctx = SSLContext.getInstance("SSL");
    } catch (NoSuchAlgorithmException e) {
      e.printStackTrace();
    }
    try {
      ctx.init(null, byPassTrustManagers, null);
    } catch (KeyManagementException e) {
      e.printStackTrace();
    }
    config.getProperties().put(HTTPSProperties.PROPERTY_HTTPS_PROPERTIES, new HTTPSProperties(hostnameVerifier, ctx));
    config.getSingletons().add(jsonProvider);

    Client client = Client.create(config);

    if (debugging) {
      client.addFilter(new LoggingFilter());
    }
    this.httpClient = client;
    return this;
  }
