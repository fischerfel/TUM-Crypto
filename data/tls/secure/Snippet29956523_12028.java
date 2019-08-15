    KeyStore ks = KeyStore.getInstance("JKS");
    ks.load(new FileInputStream(new File(keystore)), keystorepass);

    KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
    kmf.init(ks, keypassword);

    SSLContext sslcontext = SSLContext.getInstance("TLSv1.2");
    sslcontext.init(kmf.getKeyManagers(), null, null);

    ServerSocketFactory ssf = sslcontext.getServerSocketFactory();
    SSLServerSocket server = (SSLServerSocket)ssf.createServerSocket(65000);

    Socket connectionSocket = null;
    BufferedReader in = null;
    String requestedFile = null;
    boolean getRequest = true;

    while(getRequest || requestedFile == null)
    {
        try
        {
            System.out.println("Web Server: Listening for connection from Web browser");
            connectionSocket = server.accept();
            System.out.println("Web Server: Connection Established");

            in = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));

            requestedFile = in.readLine();
            System.out.println(requestedFile);
            getRequest = false;
        }
        catch(Exception e)
        {
            connectionSocket.close();
        }

        if(requestedFile != null)
            System.out.println(requestedFile);

        //Here is where the NullPointerException occurs
        requestedFile = requestedFile.substring(requestedFile.indexOf("GET") + 4,
                requestedFile.indexOf("HTTP"));
