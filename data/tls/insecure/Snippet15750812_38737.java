private void configureServiceClient(ServiceClient client) {
       SSLContext ctx;
        try {
            KeyStore truststore = KeyStore.getInstance("JKS");
            truststore.load(getClass().getResourceAsStream("/truststore.jks"),
                    "latitude".toCharArray());

            ctx = SSLContext.getInstance("SSL");
            TrustManagerFactory tmf = TrustManagerFactory
                    .getInstance(TrustManagerFactory.getDefaultAlgorithm());
            tmf.init(truststore);
            ctx.init(null, tmf.getTrustManagers(), null);
        } catch (Exception e) {
            logger.error("Exception loading Bold trust store", e);
            throw new RuntimeException(e);
        }

        SSLProtocolSocketFactory sslFactory = new SSLProtocolSocketFactory(ctx);
        Protocol prot = new Protocol("https",
                (ProtocolSocketFactory) sslFactory, 443);
        client.getOptions().setProperty(HTTPConstants.CUSTOM_PROTOCOL_HANDLER,
                prot);
}
