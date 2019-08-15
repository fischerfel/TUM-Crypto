KeyStore serverKeys = KeyStore.getInstance("JKS");
            serverKeys.load(new FileInputStream("authKeystore"),"password".toCharArray());
            KeyManagerFactory serverKeyManager = KeyManagerFactory.getInstance("SunX509");
            serverKeyManager.init(serverKeys,"password".toCharArray());


            KeyStore clientPub = KeyStore.getInstance("JKS");
            clientPub.load(new FileInputStream("authTrustedStore"),"password".toCharArray());
            TrustManagerFactory trustManager = TrustManagerFactory.getInstance("SunX509");
            trustManager.init(clientPub);

          //use keys to create SSLSoket
          SSLContext ssl = SSLContext.getInstance("TLS");
          ssl.init(serverKeyManager.getKeyManagers(), trustManager.getTrustManagers(), SecureRandom.getInstance("SHA1PRNG"));
          serverSock = (SSLServerSocket)ssl.getServerSocketFactory().createServerSocket(8889);
          serverSock.setNeedClientAuth(true);
          socket = (SSLSocket)serverSock.accept();
