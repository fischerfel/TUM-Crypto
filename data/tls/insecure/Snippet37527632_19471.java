public void distributeExchangesRateToATM() {
    //FTPS client instance
    FTPSClient client = null;
    //Login and password for the client
    String username = "lamtouni";
    String password = "Password2016";
    try
    {
        //KeyStore represents a storage facility for cryptographic keys and certificates
        //To provide a specific keystore type
        KeyStore keyStore=KeyStore.getInstance("JKS");
        //Load this KeyStore from the given input stream.
        //A password may be given to unlock the keystore
        keyStore.load(new FileInputStream(new File(KEYSTORE_FILE_NAME)), KEYSTORE_PASS.toCharArray());


        //TrustManagerFactory class acts as a factory for trust managers based on a source of trust material
        //The trust material is based on a KeyStore
        TrustManagerFactory trustManagerFactory=TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        trustManagerFactory.init(keyStore);


        //KeyManagerFactory class acts as a factory for trust managers based on a source of key material
        //The key material is based on a KeyStore
        KeyManagerFactory keyManagerFactory=KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
        keyManagerFactory.init(keyStore, KEYSTORE_PASS.toCharArray());


        //SSLContext represent a secure socket protocol implementation which acts as a factory for secure socket factories
        //It's initialized with an optional set of key and trust managers and source of secure random (null in ower case)
        SSLContext sslContext=SSLContext.getInstance("TLS");
        sslContext.init(keyManagerFactory.getKeyManagers(), trustManagerFactory.getTrustManagers(), null);



        //SSLServerSocketFactorys create SSLServerSockets based on SSLContext created before
        SSLServerSocketFactory serverSocketFactory=sslContext.getServerSocketFactory();

        //Instantiate the client with 'false' means that we want an explicit Secure protocol
        //And with 'sslContext'
        client = new FTPSClient(false,sslContext);
        //Show the FTP Commands that the FTPS Client uses in console
        client.addProtocolCommandListener(new PrintCommandListener(new  PrintWriter(System.out)));
        client.setServerSocketFactory(serverSocketFactory);
        List<ATM> atms=atmRepository.findAll();
        for (ATM atm : atms) {

            if (atm.getAtmStatus().equals("ACTIVE")){


        client.connect(atm.getAtmIP(), 21);
        logger.info("Connecting to the server "+atm.getAtmIP());


        //Authentication with TLS protocol
        client.setAuthValue("TLS");

        //Sets the file type to be transferred by he default settings
        client.setFileType(FTP.NON_PRINT_TEXT_FORMAT);
        client.execPBSZ(0);  // Set protection buffer size This command sets the maximum size, in bytes, of the encoded data blocks to be sent or received during file transfer
        client.execPROT("P"); // Set data channel protection to private
        client.enterLocalPassiveMode(); //Use the passive mode


        client.login(username, password);
        logger.info("Login Success with "+username+" and "+password);
        System.out.println("Connected to " + atm.getAtmIP() + ".");
        int reply;

        reply = client.getReplyCode(); // to check that the connection was successful since connect

        if (!FTPReply.isPositiveCompletion(reply))
        {
            client.disconnect();
            System.err.println("FTP server refused connection.");
            System.exit(1);
        }


      //File to distribute

        File LocalFile = new File("E:/Workspace Developement/prochange/exchange_rates.txt");

           String RemoteFile = paramServerRepository.getExchangeRateFile();
           InputStream inputStream = new FileInputStream(LocalFile);


                boolean done = client.storeFile(RemoteFile, inputStream);
                inputStream.close();
                if (done) {
                    System.out.println("The first file is uploaded successfully.");
                    logger.info("The file exchange_rates is distributed successfully to ATM IP :"+ atm.getAtmIP());
                }


            }
        }   
    }
    catch (Exception e)
    {
        if (client.isConnected())
        {
            try
            {
                client.disconnect();
            }
            catch (IOException ex)
            {
                ex.printStackTrace();
            }
        }
       logger.error("Could not connect to server.");
        e.printStackTrace();
        return;
    }

    }
