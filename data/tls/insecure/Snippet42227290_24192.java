public void connect() throws SmackException, IOException, XMPPException, UnrecoverableKeyException, KeyManagementException, KeyStoreException, NoSuchAlgorithmException, CertificateException 
    {

        XMPPTCPConnectionConfiguration config = XMPPTCPConnectionConfiguration.builder()
          .setUsernameAndPassword(username, password)
          .setServiceName(xmppdomain)
          .setHost(host)
          .setPort(port)
          .setCustomSSLContext(getSSLContext())
          .build();

        xmppConnection = new XMPPTCPConnection(config);
        xmppConnection.connect();

    }

    private SSLContext getSSLContext() throws KeyStoreException, NoSuchAlgorithmException, CertificateException, IOException, UnrecoverableKeyException, KeyManagementException {

        char[] JKS_PASSWORD = "password".toCharArray();
        char[] KEY_PASSWORD = "password".toCharArray();

        KeyStore keyStore = KeyStore.getInstance("JKS");
        InputStream is = new FileInputStream("<PATH_TO_JKS>");
        keyStore.load(is, JKS_PASSWORD);
        KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
        kmf.init(keyStore, KEY_PASSWORD);
        TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        tmf.init(keyStore);

        SSLContext sc = SSLContext.getInstance("TLS");
        sc.init(kmf.getKeyManagers(), tmf.getTrustManagers(), new java.security.SecureRandom());

        return sc;


    }
