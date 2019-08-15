    XMPPTCPConnectionConfiguration.Builder configBuilder = XMPPTCPConnectionConfiguration.builder();
    configBuilder.setUsernameAndPassword("bugs", "xxxxxx");
    final String l_jabber_hostname = "xxxxxxx";
    configBuilder.setServiceName(l_jabber_hostname);
    configBuilder.setHost(l_jabber_hostname);
    configBuilder.setPort(5222);
    configBuilder.setSecurityMode(ConnectionConfiguration.SecurityMode.required);
    configBuilder.setKeystorePath("/tmp/cacerts");
    configBuilder.setKeystoreType("JKS");
    configBuilder.setHostnameVerifier(new HostnameVerifier() {
      @Override
      public boolean verify(String a_hostname, SSLSession session) {
        boolean l_approved = l_jabber_hostname.equalsIgnoreCase(a_hostname);
        return l_approved;
      }
    });

    Roster.setRosterLoadedAtLoginDefault(false);

    XMPPTCPConnectionConfiguration l_config = configBuilder.build();
    AbstractXMPPConnection connection = new XMPPTCPConnection(l_config);

    connection.connect();
