    private void connect()
    {
        try
        {
            SSLSocketFactory sslFac;
            SSLSocket dataSocket = null;
            SSLSocket fileSocket = null;

            /*
             * This block is nearly identical to the security block for the server side.  
             */
            try
            {
                KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
                keyStore.load(new FileInputStream(certificateDir), password.toCharArray());

                TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
                tmf.init(keyStore);

                KeyManagerFactory kmf = KeyManagerFactory.getInstance((KeyManagerFactory.getDefaultAlgorithm()));
                kmf.init(keyStore, password.toCharArray());

                System.setProperty("https.protocols", "SSL");
                SSLContext ctx = SSLContext.getInstance("SSL");
                ctx.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);
                sslFac = ctx.getSocketFactory();

                String ip = "<THE SERVER'S IP ADDRESS>";
                dataSocket = (SSLSocket) sslFac.createSocket(ip, dataPort);
                fileSocket = (SSLSocket) sslFac.createSocket(ip, filePort);
            }
            catch(Exception e)
            {
                System.out.println("FAILED.");
                e.printStackTrace();
                System.exit(-1);
            }

            reader = new BufferedReader(new InputStreamReader(dataSocket.getInputStream()));
            writer = new PrintWriter(dataSocket.getOutputStream());
            OutputStream fileOut = fileSocket.getOutputStream();

            writer.println("CLIENT_HANDSHAKE_INIT");
            writer.flush();
            }
     }
