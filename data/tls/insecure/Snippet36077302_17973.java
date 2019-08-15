    SSLContext sc = SSLContext.getInstance("SSL");
    KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
    KeyStore ks = KeyStore.getInstance("JKS");
    InputStream is = getClass().getResourceAsStream("/path_to_client_certificate.p12");

    Assert.assertNotNull(is);

    ks.load(is, "my_password".toCharArray());
    kmf.init(ks, "my_password".toCharArray());
    sc.init(kmf.getKeyManagers(), null, null);
    HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

    URL url = getClass().getClassLoader().getResource("/path_to_local_wsdl_file.wsdl");
    BillingService billingService = new BillingService(url);
    BoBillingService boBillingService = billingService.getWsHttp();

    BindingProvider bindingProvider = (BindingProvider) boBillingService;
    bindingProvider.getRequestContext()
            .put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY,
                    "https://www.hostname.com/service_path");

    ExecutePingOut executePingOut = boBillingService.executePing("My Text", true);

    LOGGER.trace(executePingOut.getMessage().getValue());
