long sizeSend = 0;
    SSLContext sc = null;

    try {
        sc = SSLContext.getInstance("TLS");
        sc.init(null, TRUST_ALL_CERTS, new SecureRandom());
    } catch (NoSuchAlgorithmException | KeyManagementException e) {
        LOGGER.error("Failed to create SSL context", e);
    }

    // Ignore differences between given hostname and certificate hostname
    HostnameVerifier hv = (hostname, session) -> true;

    // Create the REST client and configure it to connect meta
    Client client = ClientBuilder.newBuilder()
            .hostnameVerifier(hv)
            .sslContext(sc).build();

    WebTarget baseTarget = client.target(getURL()).path(HTTP_PATH);
    Response jsonResponse = null;

    try {
        StringBuilder eventsBatchString = new StringBuilder();
        eventsBatchString.append(this.getEvent(event));
        Entity<String> entity = Entity.entity(eventsBatchString.toString(), MediaType.APPLICATION_JSON_TYPE);
        builder = baseTarget.request();
        LOGGER.debug("about to send the event {} and URL {}", entity, getURL());
        jsonResponse = builder.header(HTTP_ACK_CHANNEL, guid.toString())
                .header("Content-type", MediaType.APPLICATION_JSON)
                .header("Authorization", String.format("Meta %s", eventsModuleConfig.getSecretKey()))
                .post(entity);
