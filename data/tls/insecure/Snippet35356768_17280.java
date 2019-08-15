    config=XMPPTCPConnectionConfiguration.builder();
    //config.setUsernameAndPassword("ramsey", "123");
    config.setResource("smack");
    config.setServiceName("*MY Domain*");
    config.setHost("*MY Domain*");
    config.setPort(5222);

    config.setSecurityMode(ConnectionConfiguration.SecurityMode.ifpossible);
    SSLContext sc = SSLContext.getInstance("TLS");
    sc.init(new KeyManager[0], new TrustManager[]{new TLSUtils.AcceptAllTrustManager()}, new SecureRandom());
    config.setCustomSSLContext(sc);

    conn = new XMPPTCPConnection(config.build());

    conn.connect();  
