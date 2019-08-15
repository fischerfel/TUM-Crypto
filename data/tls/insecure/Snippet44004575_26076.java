public static void connectToService(final String user, final char[] pass, boolean https, Object port,
                                    String url, String trustStorePath, char[] trustStorePassword, int responseTimeoutSeconds, 
                                    int connTimeoutSeconds) throws Exception {
    if (responseTimeoutSeconds==0) {
        responseTimeoutSeconds = 120;
    }
    if (connTimeoutSeconds==0) {
        connTimeoutSeconds = 120;
    }

    if (port==null) {
        throw new IllegalArgumentException("Service should not be null here. Cannot connect to service.");
    }

    if (user!=null) {//set authentication only if there is username set.

        Authenticator auth = new Authenticator() {
            public PasswordAuthentication getPasswordAuthentication() {
                logger.info("REQUESTING AUTHENTICATION!");
                logger.debug("Username: {}", user);
                return (new PasswordAuthentication(user,
                        pass));
            }
        };
        Authenticator.setDefault(auth);
    }

    org.apache.cxf.endpoint.Client client = org.apache.cxf.frontend.ClientProxy.getClient(port);

    org.apache.cxf.transport.http.HTTPConduit httpConduit = new HTTPConduit(client.getBus(), client.getEndpoint().getEndpointInfo());
    ClientImpl rightClient = new ClientImpl(client.getBus(), client.getEndpoint(), httpConduit);

    if (url != null)
        rightClient.getRequestContext().put(Message.ENDPOINT_ADDRESS, url);
    String a = (String)rightClient.getRequestContext().get(Message.ENDPOINT_ADDRESS);
    logger.info("URL: {}", a);
    if (https) {
        logger.debug("setting keystore");
        TLSClientParameters parameters = new TLSClientParameters();
        try {
            // TODO do not use this in production, it is for testing only!
            if (trustStorePath!=null) {
                logger.debug("Trust store path set to: {}", trustStorePath);
                KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
                trustStore.load(new FileInputStream(trustStorePath), trustStorePassword);
                logger.debug("Trust store loaded");
                TrustManagerFactory tmf = TrustManagerFactory.getInstance("SunX509");
                tmf.init(trustStore);
                logger.debug("Trust store factory initialized");

                SSLContext sslContext = SSLContext.getInstance("SSL");
                sslContext.init(null, tmf.getTrustManagers(), new SecureRandom());
                logger.debug("SSLContext initialized with trust managers using SecureRandom");
                SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();
                parameters.setSSLSocketFactory(sslSocketFactory);
                logger.debug("SSLSocketFactory updated.");
            } else {
                logger.warn("Trust store path was null! SSL expected but path not provided. We will use default JAVA truststore.");
            }
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage(), e);
        }
        parameters.setDisableCNCheck(true);
        httpConduit.setTlsClientParameters(parameters);
    }


    HTTPClientPolicy httpClientPolicy = new HTTPClientPolicy();
    httpClientPolicy.setConnectionTimeout(connTimeoutSeconds*1000);
    httpClientPolicy.setAllowChunking(false);
    httpClientPolicy.setReceiveTimeout(responseTimeoutSeconds*1000);
    httpClientPolicy.setAutoRedirect(true);

    if (user!=null && pass!=null) {
        AuthorizationPolicy authorizationPolicy = new AuthorizationPolicy();
        authorizationPolicy.setUserName(user);
        authorizationPolicy.setPassword(pass.toString());
        httpConduit.setAuthorization(authorizationPolicy);
    }
    httpConduit.setClient(httpClientPolicy);
}
