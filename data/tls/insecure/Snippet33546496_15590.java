  Socket clientSocket = null;
        KeyStore store = KeyStore.getInstance("BKS");
        InputStream in2 = ctx.getResources().openRawResource(
                R.raw.server);
        store.load(in2, "password".toCharArray());

        TrustManagerFactory tmf = TrustManagerFactory
                .getInstance(KeyManagerFactory.getDefaultAlgorithm());
        tmf.init(store);
        SSLContext sslcontext = SSLContext.getInstance("SSL");
        sslcontext.init(null, tmf.getTrustManagers(),
                new SecureRandom());
        SSLSocketFactory sslsocketfactory = sslcontext
                .getSocketFactory();
        clientSocket = (SSLSocket) sslsocketfactory.createSocket(
                Constants.SERVER_HOST, port);
        ObjectInputStream obi = new ObjectInputStream(
                clientSocket.getInputStream());
        ObjectOutputStream obs = new ObjectOutputStream(
                clientSocket.getOutputStream());

        obs.writeObject("text");
        obs.flush();
