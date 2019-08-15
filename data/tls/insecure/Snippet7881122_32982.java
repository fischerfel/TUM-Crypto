        ClientConfig config = new DefaultClientConfig();
        SSLContext context = null;
        try
        {
            context = SSLContext.getInstance("SSL");
            context.init(null,
                    new TrustManager[] { new DumbX509TrustManager() },
                    null);
            config.getProperties()
                    .put(HTTPSProperties.PROPERTY_HTTPS_PROPERTIES,
                            new HTTPSProperties(this.getHostnameVerifier(),
                                    context));
            webClient = Client.create(config);
        }
        ....
