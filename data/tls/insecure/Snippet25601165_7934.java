SSLServerSocketFactory ssf = null;
    try {
        // set up key manager to do server authentication
        SSLContext ctx;
        KeyManagerFactory kmf;
        KeyStore ks;
        char[] passphrase = "passphrase".toCharArray();

        ctx = SSLContext.getInstance("TLS");
        kmf = KeyManagerFactory.getInstance("SunX509");
        ks = KeyStore.getInstance("JKS");

        ks.load(new FileInputStream("fsKeystore"), passphrase);
        kmf.init(ks, passphrase);
        ctx.init(kmf.getKeyManagers(), null, null);

        ssf = ctx.getServerSocketFactory();

        System.out.println("Waiting for Connection");
        SSLServerSocket sslsocketServer = (SSLServerSocket) ssf.createServerSocket(9999);
        SSLSocket sslsocket = (SSLSocket) sslsocketServer.accept();
        sslsocket.addHandshakeCompletedListener(new HandshakeCompletedListener() {

            @Override
            public void handshakeCompleted(HandshakeCompletedEvent arg0) {
                System.out.println("Handshake finished");
            }
        });
        InputStream inputstream = sslsocket.getInputStream();
        InputStreamReader inputstreamreader = new InputStreamReader(inputstream);
        BufferedReader bufferedreader;
        bufferedreader = new BufferedReader(inputstreamreader);

        String string = "";
        while ((string = bufferedreader.readLine()) != null) {
            string = bufferedreader.readLine();
            System.out.println(System.currentTimeMillis());
            System.out.println(string);
            System.out.flush();
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
