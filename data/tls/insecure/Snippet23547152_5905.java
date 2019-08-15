        String keyStoreType = KeyStore.getDefaultType();
        KeyStore keyStore = KeyStore.getInstance(keyStoreType);
        InputStream inputStream = context.getResources().openRawResource(R.raw.server);
        keyStore.load(inputStream, "passwd".toCharArray());

        String keyalg = KeyManagerFactory.getDefaultAlgorithm();
        kmf.init(keyStore, "passwd".toCharArray());

        KeyManagerFactory kmf = KeyManagerFactory.getInstance(keyalg);
        SSLContext context = SSLContext.getInstance("TLS");
        context.init(kmf.getKeyManagers(), null, null);
        SSLServerSocket ss = (SSLServerSocket) context.getServerSocketFactory().createServerSocket();
        inputStream.close();

        //Here comes the error
        ss.accept();
