    try {
        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(null, null, null);
        SslFilter sslFilter = new SslFilter(sslContext);
        getService().getFilterChain().addFirst("sslFilter", sslFilter);
    } catch (NoSuchAlgorithmException | KeyManagementException e) {
        log.warn("Failed to establish SSLFitler");
        e.printStackTrace();
    }
