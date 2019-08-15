    static final String GCM_SERVER = "gcm.googleapis.com";
    static final int GCM_PORT = 5235;
    private XMPPTCPConnection connection;
    private SSLContext sslCtx;

    ...

    try {
        KeyStore windowsRootTruststore = KeyStore.getInstance("Windows-ROOT", "SunMSCAPI");
        windowsRootTruststore.load(null, null);
        TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        tmf.init(windowsRootTruststore);
        sslCtx = SSLContext.getInstance("TLS");
        sslCtx.init(null, tmf.getTrustManagers(), null);
    } catch (KeyStoreException | NoSuchProviderException | NoSuchAlgorithmException
            | KeyManagementException | CertificateException e) {
        e.printStackTrace();
    }

    ...

    XMPPTCPConnectionConfiguration config = XMPPTCPConnectionConfiguration.builder()
        .setHost(GCM_SERVER)
        .setPort(GCM_PORT)
        .setServiceName(GCM_SERVER)
        .setUsernameAndPassword(GCM_SENDER_ID + "@gcm.googleapis.com", GCM_PASSWORD)
        .setCompressionEnabled(false)
        .setSecurityMode(SecurityMode.ifpossible)
        .setSendPresence(false)
        .setSocketFactory(sslCtx.getSocketFactory())
        .build();
    connection = new XMPPTCPConnection(config);
    Roster roster = Roster.getInstanceFor(connection);
    roster.setRosterLoadedAtLogin(false);
    connection.addConnectionListener(this);
    connection.addAsyncStanzaListener(this, new StanzaTypeFilter(Message.class));
    connection.addPacketInterceptor(new StanzaListener() {
            @Override
            public void processPacket(Stanza packet) throws NotConnectedException {
                System.out.println("CCS_Client sent the following message: " + packet.toXML());
            }
        }, new StanzaTypeFilter(Message.class));
    connection.connect();
    connection.login();
    System.out.println(connection.isSecureConnection());
