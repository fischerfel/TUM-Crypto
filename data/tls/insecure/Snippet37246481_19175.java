String[] enabledCipherSuites = { "SSL_DH_anon_WITH_RC4_128_MD5" };
           AssetManager aM = getAssets();
           InputStream  kSIS = aM.open("serverkeystore");
           KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
           keyStore.load(kSIS, "mypassword".toCharArray());

           TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
           tmf.init(keyStore);

           SSLContext sslC = SSLContext.getInstance("TLS");
           sslC.init(null,tmf.getTrustManagers(),null);

           SSLSocketFactory sslSF = sslC.getSocketFactory();
           s1 = (SSLSocket) sslSF.createSocket(IP,PORT);
           s1.setEnabledCipherSuites(enabledCipherSuites);
           s1.startHandshake();
