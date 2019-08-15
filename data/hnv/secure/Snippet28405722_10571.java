    XMPPTCPConnectionConfiguration.Builder config = XMPPTCPConnectionConfiguration.builder();
    config.setSecurityMode(ConnectionConfiguration.SecurityMode.required);
    //For OLD STYLE SSL
    //config.setSecurityMode(ConnectionConfiguration.SecurityMode.enabled);
    config.setUsernameAndPassword(USERNAME + "@" + DOMAIN, "PASSWORD");
    config.setServiceName(DOMAIN);
    config.setHost(DOMAIN);
    config.setPort(PORT);
    config.setDebuggerEnabled(true);
    //OLD STYLE SSL
    //config.setSocketFactory(SSLSocketFactory.getDefault());

    try {
        SSLContext sc = SSLContext.getInstance("TLS");
        MemorizingTrustManager mtm = new MemorizingTrustManager(ctx);
        sc.init(null, MemorizingTrustManager.getInstanceList(ctx), new SecureRandom());
        config.setCustomSSLContext(sc);
        config.setHostnameVerifier(mtm.wrapHostnameVerifier(new org.apache.http.conn.ssl.StrictHostnameVerifier()));
    } catch (NoSuchAlgorithmException | KeyManagementException e) {
        throw new IllegalStateException(e);
    }

    mConnection = new XMPPTCPConnection(config.build());
    mConnection.setPacketReplyTimeout(10000);

    try {
        mConnection.connect();
        mConnection.login();
    } catch (SmackException | IOException | XMPPException e) {
        e.printStackTrace();
    }
