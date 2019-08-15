final SSLContext context = SSLContext.getInstance("TLSv1.2");
context.init(keyManagerFactory.getKeyManagers(), trustManagerFactory.getTrustManagers(), null);
SSLContext.setDefault(context);
final String[] enabledCipherSuites = { "TLS_ECDHE_ECDSA_WITH_AES_256_CBC_SHA384" };
final SSLParameters defaultParams = context.getDefaultSSLParameters();
defaultParams.setCipherSuites(enabledCipherSuites);
final SSLParameters supportedParams = context.getSupportedSSLParameters();
supportedParams.setCipherSuites(enabledCipherSuites);

ConnectionFactory factory = new ConnectionFactory();
factory.useSslProtocol(context);
factory.setHost(RABBITMQ_ADMIN_HOST);
factory.setPort(RABBITMQ_CLIENT_PORT_SSL);
factory.setUsername(RABBITMQ_ADMIN_USER);
factory.setPassword(RABBITMQ_ADMIN_PWD);
return factory.newConnection();
