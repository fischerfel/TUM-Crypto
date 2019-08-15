        KeyStore keyStore = KeyStore.getInstance("JKS");
        String storeFile = pathToStores + "/" + keyStoreFile;
        InputStream fileInput = new FileInputStream(storeFile);
        keyStore.load(fileInput, passwd.toCharArray());
        fileInput.close();

        KeyManagerFactory keyManagerFac = KeyManagerFactory.getInstance("SunX509");
        keyManagerFac.init(keyStore, passwd.toCharArray());

        SSLContext sslContext = SSLContext.getInstance("TLSv1");

        String trustFile = pathToStores + "/" + trustStoreFile;
        TrustManager [] trustManager = new TrustManager[]{new ReloadableX509TrustManager(trustFile, passwd)};
        sslContext.init(keyManagerFac.getKeyManagers(), trustManager, null);

        SSLServerSocketFactory sslssf = (SSLServerSocketFactory) SSLServerSocketFactory
                .getDefault();
        SSLServerSocket sslServerSocket = (SSLServerSocket) sslssf.createServerSocket(theServerPort);
        sslServerSocket.setNeedClientAuth(false);

        SSLSocket sslSocket = (SSLSocket) sslServerSocket.accept();
        String[] ciphers = sslSocket.getEnabledCipherSuites();

        InputStream sslIn = sslSocket.getInputStream();
        BufferedReader bufferReader = new BufferedReader(new InputStreamReader(sslIn));

        String str = null;
        while ((str = bufferReader.readLine()) != null){
            System.out.println(str);
            System.out.flush();
        }

        sslSocket.close();
