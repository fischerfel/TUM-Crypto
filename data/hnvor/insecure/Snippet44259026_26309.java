    // Create a connection to the jabber.org server.
    // Create the configuration for this new connection
    InetAddress addr = null;
    try {
        addr = InetAddress.getByName("192.***.**.**");
    } catch (UnknownHostException e) {
        e.printStackTrace();
    }
    HostnameVerifier verifier = new HostnameVerifier() {
        @Override
        public boolean verify(String hostname, SSLSession session) {
            return false;
        }
    };
    DomainBareJid serviceName = null;
    try {
        serviceName = JidCreate.domainBareFrom("localhost");
    } catch (XmppStringprepException e) {
        e.printStackTrace();
    }
    XMPPTCPConnectionConfiguration config = 

XMPPTCPConnectionConfiguration.builder()
                .setUsernameAndPassword(USER_CURRENT_USER, "password")
                .setPort(5222)
                .setSecurityMode(ConnectionConfiguration.SecurityMode.disabled)
                .setXmppDomain(serviceName)
                .setHostnameVerifier(verifier)
                .setHostAddress(addr)
                .setDebuggerEnabled(true)
                .build();

        connection = new XMPPTCPConnection(config);
        try {
            connection.connect();
        } catch (SmackException | IOException | InterruptedException | XMPPException e) {
            e.printStackTrace();
        }
