public class XMPPClient {

    private static final String XMPP_RESOURCE_IDENTIFIER = "XMPPHolodeckClient";

    public static void main(String[] args) throws InterruptedException, XMPPException, IOException, SmackException, NoSuchAlgorithmException, KeyManagementException {
        XMPPClient xmppClient = new XMPPClient("sqavm2354.ext-inc.com", "user1", "aaaa", 5222);
        xmppClient.connect();
        Thread.sleep(1000 * 20);
        xmppClient.disconnect();
    }

    AbstractXMPPConnection xmppConnection;

    String username;
    String password;
    String host;
    Integer port;

    public XMPPClient(String host, String username, String password, Integer port) {
        this.host = host;
        this.username = username;
        this.password = password;
        this.port = port;
    }

    private XMPPTCPConnectionConfiguration buildConnection() throws NoSuchAlgorithmException, KeyManagementException {
        TrustManager[] trustManagers = new TrustManager[]{
            new X509TrustManager() {
                public void checkClientTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {}

                public void checkServerTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {}

                public X509Certificate[] getAcceptedIssuers() {
                    return null;
                }
            }
        };

        SSLContext ssl = SSLContext.getInstance("SSL");
        ssl.init(null, trustManagers, new java.security.SecureRandom());

        XMPPTCPConnectionConfiguration.Builder config = XMPPTCPConnectionConfiguration.builder();
        config.setUsernameAndPassword(username, password);
        config.setResource(XMPP_RESOURCE_IDENTIFIER);
        config.setServiceName("sqavm2354.ext-inc.com");
        config.setCustomSSLContext(ssl);
        config.setHost(host);
        config.setPort(port);
        config.setDebuggerEnabled(true);
        return config.build();
    }

    public void connect() throws IOException, XMPPException, SmackException, KeyManagementException, NoSuchAlgorithmException, InterruptedException {
        // Build connection config
        XMPPTCPConnectionConfiguration config = buildConnection();

        // Create the connection
        xmppConnection = new XMPPTCPConnection(config);

        // Increase packet timeouts
        xmppConnection.setPacketReplyTimeout(30000);

        // Connect to the server
        xmppConnection.connect();

        // Log into the server
        xmppConnection.login();
    }

    public void disconnect() {
        // Disconnect from the server
        xmppConnection.disconnect();
    }
}
