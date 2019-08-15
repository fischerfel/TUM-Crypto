KeyStore keyStore = KeyStore.getInstance("JKS");
java.io.FileInputStream keyStoreInputStream = new java.io.FileInputStream("/home/tom/myKeyStore.jks");
keyStore.load (keyStoreInputStream, password_char_array);

KeyStore trustStore trustStore = KeyStore.getInstance ("JKS");
java.io.FileInputStream trustStoreInputStream = new java.io.FileInputStream("/home/tom/myTrustStore.jks");
trustStore.load (trustStoreInputStream, password_char_array);

keyStoreInputStream.close();
trustStoreInputStream.close();

KeyManagerFactory keyManagerFactory = 
  KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
TrustManagerFactory trustManagerFactory = 
  TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
keyManagerFactory.init(keyStore,password);
trustManagerFactory.init(trustStore);

SSLContext sslContext = SSLContext.getInstance("TLSv1"); 
sslContext.init(keyManagerFactory.getKeyManagers(), 
  trustManagerFactory.getTrustManagers(), 
  null);
SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory(); 

// classes for JMS
myJmsConnectionFactory.setObjectProperty(
  WMQConstants.WMQ_SSL_SOCKET_FACTORY, sslSocketFactory);

// classes for Java
MQEnvironment.sslSocketFactory = sslSocketFactory;
