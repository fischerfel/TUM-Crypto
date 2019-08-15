  HttpsURLConnection.setDefaultHostnameVerifier(
        new HostnameVerifier() {
           public boolean verify(String hostname, SSLSession sslSession) {
              return true;
           }
        }
  );

  TrustManager[] trustAllCerts = new TrustManager[] {
        new X509TrustManager() {
           public X509Certificate[] getAcceptedIssuers() {
              return new X509Certificate[0];
           }

           public void checkClientTrusted(X509Certificate[] certs, String authType) {}

           public void checkServerTrusted(X509Certificate[] certs, String authType) {}
        }
  };


  SSLContext sc = SSLContext.getInstance("SSL");
  sc.init(null, trustAllCerts, new java.security.SecureRandom());
  HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
      char[] keyPassword =  "MY_PASSWORD".toCharArray();
      KeyStore keystore = KeyStore.getInstance("PKCS12");;
      keystore.load(this.getClass().getClassLoader().getResourceAsStream("MY_CERT.p12"), keyPassword);

      KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
      keyManagerFactory.init(keystore, keyPassword);      
      KeyManager keyManagers[] = keyManagerFactory.getKeyManagers();

      Enumeration aliases = keystore.aliases();
      String keyAlias = "";
      while (aliases.hasMoreElements()) {
          keyAlias = (String) aliases.nextElement();
          System.out.println("KEY FOUND: " + keyAlias);
      }

      SSLContext sc = SSLContext.getInstance("TLS"); 
      sc.init(keyManagers, null, null);

      SSLServerSocketFactory sslContextFactory = (SSLServerSocketFactory) sc.getServerSocketFactory();
      SSLServerSocket ssl = (SSLServerSocket) sslContextFactory.createServerSocket(32567);     
      ssl.setEnabledProtocols(new String[] {"TLSv1", "TLSv1.1", "TLSv1.2", "SSLv3"});
      ssl.setEnabledCipherSuites(sslContextFactory.getSupportedCipherSuites());
