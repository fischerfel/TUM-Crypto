SSLContext context = SSLContext.getInstance("TLS");
// getKeyManagers / getTrustManagers retrieves an 
// array containing the custom key and trust manager
// instances:
KeyManager[] km = getKeyManagers();
TrustManager[] tm = getTrustManagers();
context.init(km, tm, null);

SslContextFactory contextFactory = new SslContextFactory();
contextFactory.setContext(context);

WebSocketClient client = new WebSocketClient(contextFactory);
SimpleEchoClient echoClient = new SimpleEchoClient();

try { 
    client.start();
    ClientUpgradeRequest request = new ClientUpgradeRequest();
    Future<Session> connection = client.connect(echoClient, uri, request);

    Session session = connection.get();

    // if everything works, do stuff here

    session.close();
    client.destroy();
} catch (Exception e) {
    LOG.error(e);
}
