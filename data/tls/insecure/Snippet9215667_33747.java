private KeyManager[] getKeyManagers() throws IOException, GeneralSecurityException {

        // First, get the default KeyManagerFactory.
        String alg = KeyManagerFactory.getDefaultAlgorithm();
        KeyManagerFactory kmFact = KeyManagerFactory.getInstance(alg);

        // Next, set up the KeyStore to use. We need to load the file into
        // a KeyStore instance.

        File keyFile = new File("privatekey1");

        FileInputStream fis = new FileInputStream(keyFile);
        LogManager.log("Loaded keystore privatekey1 " + keyFile.getAbsolutePath(),
LogManager.LOG_LOWEST_LEVEL);
        KeyStore ks = KeyStore.getInstance("jks");
        String keyStorePassword = "password";
        ks.load(fis, keyStorePassword.toCharArray());
        fis.close();
        // Now we initialise the KeyManagerFactory with this KeyStore
        kmFact.init(ks, keyStorePassword.toCharArray());


        KeyManagerFactory dkmFact = KeyManagerFactory.getInstance(alg);

        File keyFileTwo = new File("privatekey2");

        FileInputStream fisTwo = new FileInputStream(keyFileTwo);
        LogManager.log("Loaded keystore privatekey2 " + keyFileTwo.getAbsolutePath(), LogManager.LOG_LOWEST_LEVEL);
        KeyStore ksTwo = KeyStore.getInstance("jks");
        String keyStorePasswordTwo = "password";
        ksTwo.load(fisTwo, keyStorePasswordTwo.toCharArray());
        fisTwo.close();
        // Now we initialise the KeyManagerFactory with this KeyStore
        dkmFact.init(ksTwo, keyStorePasswordTwo.toCharArray());


        // default
        //KeyManagerFactory dkmFact = KeyManagerFactory.getInstance(alg);
        //dkmFact.init(null, null);


        // Get the first X509KeyManager in the list
        X509KeyManager customX509KeyManager = getX509KeyManager(alg, kmFact);
        X509KeyManager jvmX509KeyManager = getX509KeyManager(alg, dkmFact);

        KeyManager[] km = {new MultiKeyStoreManager(jvmX509KeyManager, customX509KeyManager)};
        LogManager.log("Number of key managers registered:" + km.length, LogManager.LOG_LOWEST_LEVEL);
        return km;
    }

    /**
     * Find a X509 Key Manager compatible with a particular algorithm
     * @param algorithm
     * @param kmFact
     * @return
     * @throws NoSuchAlgorithmException
     */
    private X509KeyManager getX509KeyManager(String algorithm, KeyManagerFactory kmFact)
            throws NoSuchAlgorithmException {
        KeyManager[] keyManagers = kmFact.getKeyManagers();

        if (keyManagers == null || keyManagers.length == 0) {
            throw new NoSuchAlgorithmException("The default algorithm :" + algorithm + " produced no key managers");
        }

        X509KeyManager x509KeyManager = null;

        for (int i = 0; i < keyManagers.length; i++) {
            if (keyManagers[i] instanceof X509KeyManager) {
                x509KeyManager = (X509KeyManager) keyManagers[i];
                break;
            }
        }

        if (x509KeyManager == null) {
            throw new NoSuchAlgorithmException("The default algorithm :" + algorithm + " did not produce a X509 Key manager");
        }
        return x509KeyManager;
    }

    private void initialiseManager(int iPort) throws IOException, GeneralSecurityException {
        // Next construct and initialise a SSLContext with the KeyStore and
        // the TrustStore. We use the default SecureRandom.

        // load your key store as a stream and initialize a KeyStore
        File trustFile = new File("publicKey");
        LogManager.log("Trust File Loaded " + trustFile.getAbsolutePath(), LogManager.LOG_LOWEST_LEVEL);

        InputStream trustStream = new FileInputStream(trustFile);
        KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());

        // if your store is password protected then declare it (it can be null however)
        char[] trustPassword = "password".toCharArray();

        // load the stream to your store
        trustStore.load(trustStream, trustPassword);

        File trustFileTwo = new File("publicKeyTwo");
        LogManager.log("Trust File Loaded " + trustFileTwo.getAbsolutePath(), LogManager.LOG_LOWEST_LEVEL);

        InputStream trustStreamTwo = new FileInputStream(trustFileTwo);
        KeyStore trustStoreTwo = KeyStore.getInstance(KeyStore.getDefaultType());

        // if your store is password protected then declare it (it can be null however)
        char[] trustPasswordTwo = "password".toCharArray();

        // load the stream to your store
        trustStoreTwo.load(trustStreamTwo, trustPasswordTwo);


        // initialize a trust manager factory with the trusted store
        TrustManagerFactory trustFactory =
                TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        trustFactory.init(trustStore);
        trustFactory.init(trustStoreTwo);

        // get the trust managers from the factory
        TrustManager[] managers = trustFactory.getTrustManagers();


        SSLContext context = SSLContext.getInstance("SSL");
        context.init(getKeyManagers(), managers, new SecureRandom());
        SSLContext.setDefault(context);

        SSLServerSocketFactory sslSrvFact = (SSLServerSocketFactory) SSLServerSocketFactory.getDefault();

        serverSocket = sslSrvFact.createServerSocket(iPort);
        // this method didn't create a Socket Connection correctly 

    }

    class MultiKeyStoreManager implements X509KeyManager {

        private final X509KeyManager jvmKeyManager;
        private final X509KeyManager customKeyManager;

        public MultiKeyStoreManager(X509KeyManager jvmKeyManager, X509KeyManager customKeyManager) {
            this.jvmKeyManager = jvmKeyManager;
            this.customKeyManager = customKeyManager;
        }

        @Override
        public String chooseClientAlias(String[] keyType, Principal[] issuers, Socket socket) {
            // try the first key manager
            String alias = customKeyManager.chooseClientAlias(keyType, issuers, socket);
            if (alias == null) {
                alias = jvmKeyManager.chooseClientAlias(keyType, issuers, socket);
                LogManager.log("Reverting to JVM CLIENT alias : " + alias, LogManager.LOG_LOWEST_LEVEL);
            }

            return alias;

        }

        @Override
        public String chooseServerAlias(String keyType, Principal[] issuers, Socket socket) {
            // try the first key manager
            String alias = customKeyManager.chooseServerAlias(keyType, issuers, socket);
            if (alias == null) {
                alias = jvmKeyManager.chooseServerAlias(keyType, issuers, socket);
                LogManager.log("Reverting to JVM Server alias : " + alias ,LogManager.LOG_LOWEST_LEVEL);
            }
            return alias;
        }

        @Override
        public String[] getClientAliases(String keyType, Principal[] issuers) {
            String[] cAliases = customKeyManager.getClientAliases(keyType, issuers);
            String[] jAliases = jvmKeyManager.getClientAliases(keyType, issuers);
            LogManager.log("Supported Client Aliases Custom: " + cAliases.length + " JVM : " + jAliases.length,
                    LogManager.LOG_LOWEST_LEVEL);
            return (String[]) ArrayUtils.addAll(cAliases, jAliases);
        }

        @Override
        public PrivateKey getPrivateKey(String alias) {
            PrivateKey key = customKeyManager.getPrivateKey(alias);
            if (key == null) {
                System.out.println("Reverting to JVM Key : " + alias);
                return jvmKeyManager.getPrivateKey(alias);
            } else {
                return key;
            }
        }

        @Override
        public String[] getServerAliases(String keyType, Principal[] issuers) {
            String[] cAliases = customKeyManager.getServerAliases(keyType, issuers);
            String[] jAliases = jvmKeyManager.getServerAliases(keyType, issuers);
            LogManager.log("Supported Server Aliases Custom: " + cAliases.length + " JVM : " + jAliases.length,
                    LogManager.LOG_LOWEST_LEVEL);
            return (String[]) ArrayUtils.addAll(cAliases, jAliases);
        }

        @Override
        public java.security.cert.X509Certificate[] getCertificateChain(String string) {
            java.security.cert.X509Certificate[] chain = customKeyManager.getCertificateChain("alias_key1");
            if (chain == null || chain.length == 0) {
                LogManager.log("Reverting to JVM Chain : " + string, LogManager.LOG_LOWEST_LEVEL);
                return jvmKeyManager.getCertificateChain("alias_key2");
            } else {
                return chain;
            }
        }
    }

and this gave me this status
