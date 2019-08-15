
SSLConfig ssl = new SSLConfig(){
 @Override
 public SSLContext createSSLContext() { 
  try{
   //Load the keystore.
   KeyStore keyStore=KeyStore.getInstance(KeyStore.getDefaultType());
   InputStream keyStream=ClassLoader.getSystemResourceAsStream("my.jks");
   //InputStream keyStream=new java.net.URL("jar:file:/C:/dir/my.jar!/my.jks").openStream();
   keyStore.load(keyStream,"mypassword");
   keyStream.close();

   //Create the factory from the keystore.
   String kmfAlgorithm=System.getProperty("ssl.KeyManagerFactory.algorithm",KeyManagerFactory.getDefaultAlgorithm());
   KeyManagerFactory keyManagerFactory=KeyManagerFactory.getInstance(kmfAlgorithm);
   keyManagerFactory.init(keyStore,"mypassword");

   //Create the SSLContext
   SSLContext sslContext=SSLContext.getInstance("TLS");
   sslContext.init(keyManagerFactory.getKeyManagers(), null, null);
   return sslContext;
  }

  //Wrap all Exceptions in a RuntimeException.
  catch(Exception e){
   throw new RuntimeException(e);
  }
 }
};
