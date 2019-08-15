XMPPTCPConnectionConfiguration.Builder config = XMPPTCPConnectionConfiguration.builder();
    config.setSecurityMode(ConnectionConfiguration.SecurityMode.disabled);
    config.setXmppDomain(serviceName);
    config.setHost(context.getString(R.string.server));
    config.setHostnameVerifier(verifier);
    config.setHostAddress(addr);
    config.setResource("Android");
    config.setPort(Integer.parseInt(context.getString(R.string.server_port)));
    config.setDebuggerEnabled(true);

    XMPPTCPConnection.setUseStreamManagementResumptionDefault(true);
    XMPPTCPConnection.setUseStreamManagementDefault(true);
    connection = new XMPPTCPConnection(config.build());
