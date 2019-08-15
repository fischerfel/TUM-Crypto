XMPPTCPConnectionConfiguration c = XMPPTCPConnectionConfiguration.builder().setHost("api.coredial.com")
            .setHostAddress(InetAddress.getByName("api.coredial.com")).setDebuggerEnabled(true).setPort(5222)

            .setXmppDomain(String.valueOf(JidCreate.bareFrom("api.coredial.com")))
            .setUsernameAndPassword(userName,password)


            .setHostnameVerifier(new HostnameVerifier() {
                public boolean verify(String s, SSLSession sslSession) {
                    return true;
                }
            }).build();


    XMPPTCPConnection con = new XMPPTCPConnection(c);


    // Connect to the server
    con.connect();
    //con.login();
    con.login(userName,password);
