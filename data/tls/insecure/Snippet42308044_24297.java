        KeyStore keystore = KeyStore.getInstance(KeyStore.getDefaultType());
        keystore.load(new FileInputStream(PATH_TO_KEYSTORE), PASSWORD.toCharArray());

        KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
        kmf.init(keystore, PASSWORD.toCharArray());

        SSLContext context = SSLContext.getInstance("TLS");
        KeyManager[] keyManagers = kmf.getKeyManagers();

        context.init(keyManagers, null, null);

        SSLServerSocketFactory factory = context.getServerSocketFactory();

        serverSocket = (SSLServerSocket) factory.createServerSocket(port);
        serverSocket.setSoTimeout(0);
