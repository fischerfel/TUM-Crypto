    SSLContext ctx = null;
    try {
        KeyStore trustStore;
        trustStore = KeyStore.getInstance("JKS");
        trustStore.load(new FileInputStream("C:\\truststore_client"),
                "asdfgh".toCharArray());
        TrustManagerFactory tmf = TrustManagerFactory
                .getInstance("SunX509");
        tmf.init(trustStore);
        ctx = SSLContext.getInstance("SSL");
        ctx.init(null, tmf.getTrustManagers(), null);
    } catch (NoSuchAlgorithmException e1) {
        e1.printStackTrace();
    } catch (KeyStoreException e) {
        e.printStackTrace();
    } catch (CertificateException e) {
        e.printStackTrace();
    } catch (FileNotFoundException e) {
        e.printStackTrace();
    } catch (IOException e) {
        e.printStackTrace();
    } catch (KeyManagementException e) {
        e.printStackTrace();
    }
    ClientConfig config = new DefaultClientConfig();
    config.getProperties().put(HTTPSProperties.PROPERTY_HTTPS_PROPERTIES,
            new HTTPSProperties(null, ctx));

    WebResource service = Client.create(config).resource(
            "https://localhost:9999/");
    service.addFilter(new HTTPBasicAuthFilter(username, password));

    // Attempt to view the user's page.
    try {
        service.path("user/" + username).get(String.class);
    } catch (Exception e) {
        e.printStackTrace();
    }
