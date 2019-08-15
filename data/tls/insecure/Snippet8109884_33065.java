    KeyStore ks = KeyStore.getInstance("JKS");  
    ks.load(new FileInputStream("C:\\Program Files\\Java\\jre6\\bin\\cacerts"), "password".toCharArray());

    KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
    kmf.init(ks, "password".toCharArray());

    SSLContext context = SSLContext.getInstance("TLS");
    context.init(kmf.getKeyManagers(), null, null);


    ServerSocketFactory ssocketFactory = SSLServerSocketFactory.getDefault();
    serversocket = ssocketFactory.createServerSocket(port);
