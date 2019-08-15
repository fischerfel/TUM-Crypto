        KeyStore keystore = KeyStore.getInstance(KeyStore.getDefaultType());
        keystore.load(Gdx.files.internal(PATH_TO_KEYSTORE).read(), PASSWORD.toCharArray());

        TrustManagerFactory tmf = TrustManagerFactory.getInstance("SunX509");
        tmf.init(keystore);

        SSLContext context = SSLContext.getInstance("TLS");
        TrustManager[] trustManagers = tmf.getTrustManagers();

        context.init(null, trustManagers, null);

        SSLSocketFactory sslSocketFactory = context.getSocketFactory();

        sslSocket = (SSLSocket) sslSocketFactory.createSocket(address, port);
