try{                
      String keyStoreType = KeyStore.getDefaultType();
      KeyStore keyStore = KeyStore.getInstance(keyStoreType);
      keyStore.load(Dummy.class.getResourceAsStream("IPMessengerServerKeystore"), "dhar9654".toCharArray());                

      String keyalg=KeyManagerFactory.getDefaultAlgorithm();
      KeyManagerFactory kmf=KeyManagerFactory.getInstance(keyalg);
      kmf.init(keyStore, "dhar9654".toCharArray());

      SSLContext context = SSLContext.getInstance("TLS");
      context.init(MainActivity.kmf.getKeyManagers(), null, null);          
      SSLServerSocket ss=(SSLServerSocket)context.getServerSocketFactory().createServerSocket(Constants.CHAT_SERVER_PORT);

  }catch(Exception e){
     e.printStackTrace();
   }     
