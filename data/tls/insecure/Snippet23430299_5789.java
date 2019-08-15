private SSLSocketFactory getFactory( File pKeyFile, String pKeyPassword ) throws ... {
      KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance("SunX509);
      KeyStore keyStore = KeyStore.getInstance("PKCS12");

      InputStream keyInput = new FileInputStream(pKeyFile);
      keyStore.load(keyInput, pKeyPassword.toCharArray());
      keyInput.close();

      keyManagerFactory.init(keyStore, pKeyPassword.toCharArray());

      SSLContext context = SSLContext.getInstance("TLS");
      context.init(keyManagerFactory.getKeyManagers(), null, new SecureRandom());

      return context.getSocketFactory();
    }

    URL url = new URL("someurl");
    HttpsURLConnection con = (HttpsURLConnection) url.openConnection();
    con.setSSLSocketFactory(getFactory(new File("file.p12"), "secret"));
