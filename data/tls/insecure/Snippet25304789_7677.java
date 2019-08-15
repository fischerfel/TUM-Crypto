private static ConnectionFactory getConnectionFactoryForQueue(){
        com.rabbitmq.client.ConnectionFactory connectionFactory = new com.rabbitmq.client.ConnectionFactory();
        connectionFactory.setUsername("user");
        connectionFactory.setHost("MyIpAddress.0.1.1");
        connectionFactory.setPassword("pass");
        connectionFactory.setPort(5671);
        connectionFactory.setVirtualHost("/");
        SsmProtos.SSLDetails ssl = listener.getSslDetails();
        char[] keyPassphrase = "keyPassPhrase".toCharArray();
        try {
            KeyStore ks = KeyStore.getInstance("PKCS12");
            ks.load(new FileInputStream("path/to/keycert.p12"), keyPassphrase);

            KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
            kmf.init(ks, keyPassphrase);

            char[] trustPassphrase = "trustPassPhrase".toCharArray();
            KeyStore tks = KeyStore.getInstance("JKS");
            tks.load(new FileInputStream("path/to/trust/store"), trustPassphrase);

            TrustManagerFactory tmf = TrustManagerFactory.getInstance("SunX509");
            tmf.init(tks);

            SSLContext c = SSLContext.getInstance("SSLv3");
            c.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);
            connectionFactory.useSslProtocol(c);

        } catch (NoSuchAlgorithmException | CertificateException | IOException | 
                UnrecoverableKeyException | KeyStoreException | KeyManagementException e) {
            throw new IllegalArgumentException("Failed Setting up SSL",e);
        }
    return new CachingConnectionFactory(connectionFactory);
}
