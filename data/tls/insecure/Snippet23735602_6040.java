    SSLContext context = SSLContext.getInstance("SSL");
    KeyManagerFactory kmf =  
    KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
    KeyStore ks = KeyStore.getInstance("JKS");
    char[] password = "daniere".toCharArray();
    ks.load(new FileInputStream("C:/Program Files/Java/jre7/bin/keystore.jks"),password);
    kmf.init(ks,password);
    context.init(kmf.getKeyManagers(), null, null);
    SSLServerSocketFactory factory = context.getServerSocketFactory();
    SSLServerSocket server = (SSLServerSocket)factory.createServerSocket(1000);
    System.out.println("Server ready for conncetions...");
    while (true)
    {
     SSLSocket soc = (SSLSocket) server.accept();
    BufferedReader key = new BufferedReader(new InputStreamReader(soc.getInputStream()));

    String c = null;
        while((c=key.readLine())!=null)
        {
            System.out.println(c);
            break;
        }

       }
