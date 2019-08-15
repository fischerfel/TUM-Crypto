        char[] passw = "password".toCharArray();
        KeyStore ks = KeyStore.getInstance("JKS", "SUN");
        ks.load(new FileInputStream ( "mykeystore" ), passw );

        KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
        kmf.init(ks, passw);

        TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        tmf.init(ks);
        TrustManager[] tm = tmf.getTrustManagers();

        SSLContext sclx = SSLContext.getInstance("TLS");
        sclx.init( kmf.getKeyManagers(), tm, null);

        SSLSocketFactory factory = sclx.getSocketFactory();
        SSLSocket socket = (SSLSocket) factory.createSocket( "192.168.1.111", 443 );
        socket.startHandshake();

        //if no exceptions are thrown in the startHandshake method, then everything is fine..
