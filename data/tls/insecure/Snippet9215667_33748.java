            // Load the key store: change store type if needed
        KeyStore ks = KeyStore.getInstance(KeyStore.getDefaultType());
        FileInputStream fis = new FileInputStream("keyStore1");
        char[] keyPass = "passw".toCharArray();
        try {
            ks.load(fis, keyPass);
        } finally {
            if (fis != null) {
                fis.close();
            }
        }

        // Get the default Key Manager
        KeyManagerFactory kmf = KeyManagerFactory.getInstance(
                KeyManagerFactory.getDefaultAlgorithm());
        kmf.init(ks, keyPass);

        final X509KeyManager origKm = (X509KeyManager) kmf.getKeyManagers()[0];
        X509KeyManager km = new X509KeyManager() {

            public String chooseServerAlias(String[] keyType, Principal[] issuers, Socket socket) {
                String alias;

                InetAddress remoteAddress = socket.getInetAddress();

                if (remoteAddress.getHostAddress().equalsIgnoreCase("11.111.111.11")) {
                    alias = "alias1";
                    LogManager.log("Reverting to JVM CLIENT alias : " + alias, LogManager.LOG_LOWEST_LEVEL);
                } else {
                    alias = "alias2";
                    LogManager.log("Reverting to JVM CLIENT alias : " + alias, LogManager.LOG_LOWEST_LEVEL);
                }
                return alias;
            }

            public String chooseClientAlias(String[] keyType, Principal[] issuers, Socket socket) {
                // try this key manager
                String alias = origKm.chooseClientAlias(keyType, issuers, socket);
                LogManager.log("Reverting to JVM CLIENT alias : " + alias, LogManager.LOG_LOWEST_LEVEL);
                return alias;
            }

            public String[] getClientAliases(String keyType, Principal[] issues) {

                String[] cAliases = origKm.getClientAliases(keyType, issues);
                LogManager.log("Supported Client Aliases : " + cAliases.length, LogManager.LOG_LOWEST_LEVEL);
                return cAliases;

            }

            public String[] getServerAliases(String keyType, Principal[] issues) {

                String[] sAliases = origKm.getServerAliases(keyType, issues);
                LogManager.log("Supported Server Aliases: " + sAliases.length, LogManager.LOG_LOWEST_LEVEL);
                return sAliases;

            }

            public String chooseServerAlias(String keyType, Principal[] issues, Socket socket) {

                // try this key manager
                String alias = origKm.chooseServerAlias(keyType, issues, socket);
                LogManager.log("Reverting to JVM Server alias : " + alias, LogManager.LOG_LOWEST_LEVEL);
                return alias;
            }

            public X509Certificate[] getCertificateChain(String keyType) {

                // here I could specify my other keystore, keystore2 how I could do this?

                // I'm thinking in the righ way when I implemented this code to get the correct private key? 

                X509Certificate[] chain = origKm.getCertificateChain("alias1");
                if (chain == null || chain.length == 0) {
                    LogManager.log("Reverting to JVM Chain : " + keyType, LogManager.LOG_LOWEST_LEVEL);

                    return origKm.getCertificateChain("alias2");
                } else {
                    return chain;
                }
            }

            public PrivateKey getPrivateKey(String alias) {

                PrivateKey key = origKm.getPrivateKey(alias);

                // here I could get my other keystore according the alias, for example 
                // keystore2 how I could do this?

                LogManager.log("Reverting to JVM Key : " + alias, LogManager.LOG_LOWEST_LEVEL);
                return key;
            }
        };

        SSLContext sslContext = SSLContext.getInstance("SSL");
        sslContext.init(new KeyManager[]{km}, null, null);

        SSLServerSocketFactory sslSrvFact = sslContext.getServerSocketFactory();
        objServerSocket = sslSrvFact.createServerSocket(iPort);
