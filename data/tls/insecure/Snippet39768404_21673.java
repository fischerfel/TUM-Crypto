SSLContext sslContext = null;
try {
    sslContext = SSLContext.getInstance("TLS");
    sslContext.init(null, null, null);
} catch (Exception e) {
    //Failed to get default ssl context with TLS enabled... something can't proceed further
}
XMPPTCPConnectionConfiguration.Builder config = XMPPTCPConnectionConfiguration.builder();
config.setConnectTimeout(CONNECTION_TIMEOUT);
config.setSendPresence(true);
config.setCustomSSLContext(sslContext);
config.setSecurityMode(ConnectionConfiguration.SecurityMode.disabled);
config.setServiceName("gcm.googleapis.com");
config.setHost("fcm-xmpp.googleapis.com");
config.setPort(5236);//not production server
config.setDebuggerEnabled(true);
config.setCompressionEnabled(true);
config.setSocketFactory(sslContext.getSocketFactory());
(mConnection = new XMPPTCPConnection(config.build())).addConnectionListener(ConnectionSession.this);
mConnection.setPacketReplyTimeout(REPLY_TIMEOUT);
mConnection.connect();
mConnection.login(userID, password); //use your app server credential here
