    // We are using two-way authentication with certificates
SASLAuthentication.registerSASLMechanism(new SASLExternalMechanism());
XmppTrustManager xmppTrustManager = null;
try {
    xmppTrustManager = new XmppTrustManager($truststore.path, $truststore.password);
} catch (final XMPPException e) {
    log.error("Unable to create trust manager", e);
    throw new...
}

final TrustManager[] trustManagers = new TrustManager[] { xmppTrustManager };
XmppKeyManager xmppKeyManager = null;
try {
    xmppKeyManager = new XmppKeyManager($keystore.path, $keystore.password);

} catch (final XMPPException e) {
    log.error("Unable to create key manager", e);
    throw new ...
}
final KeyManager[] keyManagers = new KeyManager[] { xmppKeyManager };

SSLContext sslContext = null;
try {
    sslContext = SSLContext.getInstance("TLS");
    sslContext.init(keyManagers, trustManagers, null);
} catch (final NoSuchAlgorithmException e) {
    log.error("Cannot create SSLContext instance", e);
} catch (final KeyManagementException e) {
    log.error("Cannot initialize SSLContext instance", e);
}

final XMPPTCPConnectionConfiguration config = XMPPTCPConnectionConfiguration.builder()
        .setHost($host)
        .setPort($port)
        .setServiceName($domain).setCustomSSLContext(sslContext)
        .setHostnameVerifier(new HostnameVerifier() {
            @Override
            public boolean verify(final String hostname, final SSLSession session) {
                return $domain.equals(hostname);
            }
        }).setDebuggerEnabled(false).allowEmptyOrNullUsernames().build();

final XMPPTCPConnection xmppConnection = new XMPPTCPConnection(config);
xmppConnection.setPacketReplyTimeout(30 * 1000);
// xmppConnection.addConnectionListener(connectionListener());
ReconnectionManager.getInstanceFor(xmppConnection).enableAutomaticReconnection();
xmppConnection.setUseStreamManagement(true);

xmppConnection.addConnectionListener(new ConnectionListener() {

    @Override
    public void reconnectionSuccessful() {
        log.info("Successfully reconnected to the XMPP server.");
    }

    @Override
    public void reconnectionFailed(final Exception e) {
        log.info("Failed to reconnect to the XMPP server ", e.toString());

    }

    @Override
    public void reconnectingIn(final int seconds) {
        log.debug("Reconnecting in " + seconds + " seconds.");
    }

    @Override
    public void connectionClosedOnError(final Exception e) {
        log.info("Connection to XMPP server was lost ", e.toString());
    }

    @Override
    public void connectionClosed() {
        log.info("Connection to XMPP server was closed");
    }

    @Override
    public void connected(final XMPPConnection connection) {
        log.info("connected to XMPP server");
    }

    @Override
    public void authenticated(final XMPPConnection connection, final boolean resumed) {
        log.info("Xmpp connected authenticated. is resumed: " + resumed);

    }
});

log.debug("xmppConnection bean is initialized");
return xmppConnection;
