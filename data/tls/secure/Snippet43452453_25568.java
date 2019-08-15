public void start(StreamConsumer consumer) {
    JSONObject queueInfo;
    HttpResponse<String> res = null;

    String rabbitHost = "host";
    Integer rabbitPort = 5671;

    ConnectionFactory connectionFactory = new ConnectionFactory();
    connectionFactory.setHost(rabbitHost);
    connectionFactory.setUsername(this.webUsername);
    connectionFactory.setPassword(this.webPassword);
    connectionFactory.setPort(rabbitPort);
    connectionFactory.useSslProtocol(getSSLContext());
    connection = connectionFactory.newConnection();
    channel = connection.createChannel();

    StreamConsumerProxy scp = new StreamConsumerProxy(channel, consumer);
    channel.basicConsume(rabbitQueue, false, scp);
}

private SSLContext getSSLContext() {
    SSLContext context = null;
    try {
        KeyStore tks = null;
        TrustManagerFactory tmf = TrustManagerFactory.getInstance("SunX509");
        tmf.init(tks);
        context = HostnameVerifyingSSLContext.getInstance("TLSv1.2");
        context.init(null, tmf.getTrustManagers(), null);
    } catch (Exception e1) {
        e1.printStackTrace();
    }
    return context;
}
