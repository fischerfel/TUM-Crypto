        store = KeyStore.getInstance("BKS"); 
        InputStream in = appcontext.getResources().openRawResource(R.raw.truststore);
        store.load(in, "PASSWORD".toCharArray());

        TrustManagerFactory tmf = TrustManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
        tmf.init(store);
        sslcontext = SSLContext.getInstance("TLS");
        sslcontext.init(null, tmf.getTrustManagers(), new SecureRandom());

        SSLSocketFactory sslsocketfactory = sslcontext.getSocketFactory();

        // connect to socket
        SSLSocket sslsocket = (SSLSocket) sslsocketfactory.createSocket(
                "192.168.1.16", 9999);
