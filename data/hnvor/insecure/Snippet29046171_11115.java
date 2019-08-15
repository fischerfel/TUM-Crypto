    XMPPTCPConnectionConfiguration connConfig =    XMPPTCPConnectionConfiguration
            .builder()
            .setServiceName("example.com")
            .setHost("192.168.56.101")
            .setPort(5222)
            .setCompressionEnabled(false)
            .setSecurityMode(SecurityMode.disabled)
            .setHostnameVerifier(new HostnameVerifier() {
                public boolean verify(String arg0, SSLSession arg1) {
                    return true;
                }
            })
            .setUsernameAndPassword(user, pass).build();

    connection = new XMPPTCPConnection(connConfig);
    connection.connect();
    connection.login();
