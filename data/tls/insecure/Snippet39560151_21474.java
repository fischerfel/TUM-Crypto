try {
    TrustManager[] trustAllCerts = new TrustManager[] {
       new X509TrustManager() {
    public java.security.cert.X509Certificate[] getAcceptedIssuers() {
        return null;
    }
    public void checkClientTrusted(X509Certificate[] certs, String authType) {  }

    public void checkServerTrusted(X509Certificate[] certs, String authType) {  }
    }
    };

    SSLContext sc = SSLContext.getInstance("SSL");
    sc.init(null, trustAllCerts, new SecureRandom());
    CloseableHttpClient httpClient = HttpClients.custom().setSSLHostnameVerifier(NoopHostnameVerifier.INSTANCE).setSslcontext(sc).build();

    String output = Executor.newInstance(httpClient).execute(Request.Get("https://127.0.0.1:3000/something")
                                      .connectTimeout(1000)
                                      .socketTimeout(1000)).returnContent().asString();
    } catch (Exception e) {
    }
