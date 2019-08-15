public void listen() throws Exception {
        try {

            String passphrase = "test123";

            KeyStore ks = KeyStore.getInstance("JKS");
            ks.load(new FileInputStream("server_keystore.jks"),"test123".toCharArray());
            KeyManagerFactory kmf =
                    KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
            kmf.init(ks, "test123".toCharArray());

            SSLContext sslcontext =
                    SSLContext.getInstance("TLS");

            sslcontext.init(kmf.getKeyManagers(), null, null);

            ServerSocketFactory ssf =
                    sslcontext.getServerSocketFactory();


            SSLServerSocket serverSocket = (SSLServerSocket)
                    ssf.createServerSocket(9999);

            while (true) {
                SSLSocket s = (SSLSocket) serverSocket.accept();
                handleConnection(s);
                s.close();
            }

        } catch (IOException ioe) {
            System.out.println("IOException: " + ioe);
            ioe.printStackTrace();
            System.exit(1);
        } catch (GeneralSecurityException e) {
            System.out.println("GeneralSecurityException: " + e);
            e.printStackTrace();
            System.exit(1);
        }
    }

    int packet = 0;
    protected void handleConnection(Socket server) throws Exception {
        InputStream is = server.getInputStream();
        FileOutputStream fos = new FileOutputStream(...);
        BufferedOutputStream bos = new BufferedOutputStream(fos);

        byte buffer[] = new byte[8129];
        int count = 0;
        int size = 0;
        while ((count = is.read(buffer)) > 0)
        {
            bos.write(buffer, 0, count);
        }
        bos.flush();
        bos.close();
        is.close();
    }
