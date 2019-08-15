    final SSLContext sslContext = SSLContext.getInstance("SSL");

    sslContext.init(null, new TrustManager[] { new X509TrustManager() {
            @Override
            public void checkClientTrusted(X509Certificate[] x509Certificates, String s)
                    throws java.security.cert.CertificateException {
            }
            @Override
            public void checkServerTrusted(X509Certificate[] x509Certificates, String s)
                    throws java.security.cert.CertificateException {
            }
            @Override
            public X509Certificate[] getAcceptedIssuers() {
                return new X509Certificate[0];
            }
        } }, new SecureRandom());


    final SSLConnectionSocketFactory sslConnectionSocketFactory =
            new SSLConnectionSocketFactory(sslContext, SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);

    final Registry<ConnectionSocketFactory> registry = RegistryBuilder.<ConnectionSocketFactory>create()
            .register("https", sslConnectionSocketFactory)
            .register("http", PlainConnectionSocketFactory.INSTANCE)
            .build();

    builder.using(registry);

    Client client = new JerseyClientBuilder(env)
      .using(configuration.getJerseyClient())
      .using(registry)
      .build("MyInsecureClient");
