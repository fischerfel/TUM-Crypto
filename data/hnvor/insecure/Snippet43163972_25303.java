            InetAddress addr = InetAddress.getByName("192.168.209.2");
            HostnameVerifier verifier = new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    return false;
                }
            };
            DomainBareJid serviceName = JidCreate.domainBareFrom("localhost");
            XMPPTCPConnectionConfiguration config = XMPPTCPConnectionConfiguration.builder()
                    .setHost(server) # it will be resolved by setHostAddress method
                    .setUsernameAndPassword("davood","mypass")
                    .setPort(5222)
                    .setSecurityMode(ConnectionConfiguration.SecurityMode.disabled)
                    .setXmppDomain(serviceName)
                    .setHostnameVerifier(verifier)
                    .setHostAddress(addr)
                    .setDebuggerEnabled(true)
                    .build();
            AbstractXMPPConnection conn1 = new XMPPTCPConnection(config);

            conn1.connect();

            if(conn1.isConnected()){
                Log.d("XMPP","Connected");
            }
            conn1.login();

            if(conn1.isAuthenticated()){
                Log.d("XMPP","Authenticated");
                EntityBareJid jid = JidCreate.entityBareFrom("sadegh@localhost");
                org.jivesoftware.smack.chat2.Chat chat = ChatManager.getInstanceFor(conn1).chatWith(jid);
                chat.send("Eureka, I am connected!");


            }
