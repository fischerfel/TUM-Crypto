String[] enabledCipherSuites = { "SSL_DH_anon_WITH_RC4_128_MD5" };
        //serverSkt = new ServerSocket(PORT);
        KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
        keyStore.load(getAssets().open("serverkeystore"), "mypassword".toCharArray());
        KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
        kmf.init(keyStore,"mypassword".toCharArray());
        SSLContext sslcontext = SSLContext.getInstance("TLS");
        sslcontext.init(kmf.getKeyManagers(), null, null);

        SSLServerSocketFactory sslServerSocketF = sslcontext.getServerSocketFactory();
        sslServerSkt = (SSLServerSocket) sslServerSocketF.createServerSocket(PORT);
        sslServerSkt.setEnabledCipherSuites(enabledCipherSuites);
