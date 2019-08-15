        KeyStore keyStore = KeyStore.getInstance("JKS");
        String keyFile = pathToStore + "/" + keyStoreFile;
        InputStream keyInput = new FileInputStream(keyFile);
        keyStore.load(keyInput, passwd.toCharArray());
        keyInput.close();

        KeyManagerFactory keyManagerFac = KeyManagerFactory.getInstance("SunX509");
        keyManagerFac.init(keyStore, passwd.toCharArray());

        SSLContext sslContext = SSLContext.getInstance("TLSv1");

        String trustFile = pathToStore + "/" + trustStoreFile;

        TrustManager [] trustManager = new TrustManager[]{new ReloadableX509TrustManager(trustFile, passwd)};
        sslContext.init(keyManagerFac.getKeyManagers(), trustManager, null);

        SSLSocketFactory sslsf = (SSLSocketFactory) SSLSocketFactory.getDefault();
        SSLSocket sslSocket = (SSLSocket) sslsf.createSocket(theServerName, theServerPort);
        String[] ciphers = sslSocket.getEnabledCipherSuites();

        sslSocket.addHandshakeCompletedListener(new HandShakeListener());
        sslSocket.startHandshake();

        OutputStream sslOut = sslSocket.getOutputStream();
        sslOut.write("Hello RelayServer".getBytes());
        sslOut.flush();
        sslOut.close();
        sslSocket.close();
