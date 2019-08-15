        KeyStore ks = KeyStore.getInstance("JKS");
        ks.load(new FileInputStream(sslstore), sslstorepass.toCharArray());
        KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
        kmf.init(ks, sslcertpass.toCharArray());

        SSLContext sc = SSLContext.getInstance("TLS");
        sc.init(kmf.getKeyManagers(), null, new SecureRandom());
        SSLServerSocketFactory ssf = sc.getServerSocketFactory();
        serverSocket = ssf.createServerSocket(port);

        System.out.println("Socket initialized");
