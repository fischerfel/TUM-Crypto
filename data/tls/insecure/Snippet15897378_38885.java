char []passwKey = "1234567".toCharArray();
        KeyStore ts = KeyStore.getInstance("PKCS12");
        ts.load(new FileInputStream("/home/user/Desktop/file.p12"), passwKey);
        KeyManagerFactory tmf = KeyManagerFactory.getInstance("SunX509");
        tmf.init(ts,passwKey);
        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(tmf.getKeyManagers(), null, null);
        SSLSocketFactory factory =sslContext.getSocketFactory();
        HttpsURLConnection.setDefaultSSLSocketFactory(factory);
        SSLSocket socket = (SSLSocket) factory.createSocket("www.host.com", 8883); // Create the ServerSocket
        String[] suites = socket.getSupportedCipherSuites();
        socket.setEnabledCipherSuites(suites);
        socket.startHandshake();
