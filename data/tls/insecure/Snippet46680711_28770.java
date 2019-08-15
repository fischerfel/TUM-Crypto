    Properties systemProps = System.getProperties();
    systemProps.put("javax.net.debug","ssl");
    systemProps.put("javax.net.ssl.trustStore","<path to trustore>");
    systemProps.put("javax.net.ssl.trustStorePassword","password");
    System.setProperties(systemProps);

    SSLContext sslcontext;
    KeyStore keyStore;
    final char[] JKS_PASSWORD = "password".toCharArray();
    final char[] KEY_PASSWORD = "password".toCharArray();

    try {
        final InputStream is = new FileInputStream("<path_to_keystore.pkcs12>");
        keyStore = KeyStore.getInstance("pkcs12");
        keyStore.load(is, JKS_PASSWORD);
        final KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
        kmf.init(keyStore, KEY_PASSWORD);

        sslcontext=SSLContext.getInstance("TLS");
        sslcontext.init(kmf.getKeyManagers(), null, new java.security.SecureRandom());
    } catch (Exception ex) {
        throw new IllegalStateException("Failure initializing default SSL context", ex);
    }

    SSLSocketFactory sslsocketfactory = sslcontext.getSocketFactory();  
    DataOutputStream os = null;

    try {
        SSLSocket sslsocket = (SSLSocket) sslsocketfactory.createSocket();
        sslsocket.connect(new InetSocketAddress(host, port), connectTimeout);
        sslsocket.startHandshake();

        os = new DataOutputStream(sslsocket.getOutputStream());
        // log.info("Sending echo packet");
        String toSend = "{\"echo\":\"echo\"}";
        os.writeBytes(toSend);
    } catch (UnknownHostException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    } catch (IOException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    } catch (InterruptedException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    }
