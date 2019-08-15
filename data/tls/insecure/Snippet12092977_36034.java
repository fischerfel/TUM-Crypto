    SSLContext context = SSLContext.getInstance("SSL");
    context.init(kms, trustAllCerts, null);
    SSLContext.setDefault(context);

    ClientConfig config = new DefaultClientConfig();
    config.getProperties().put(ClientConfig.PROPERTY_READ_TIMEOUT, timeout * 1000);
    config.getProperties().put(ClientConfig.PROPERTY_FOLLOW_REDIRECTS, false);
    config.getFeatures().put(ClientConfig.FEATURE_DISABLE_XML_SECURITY, true);
    client = Client.create(config);
