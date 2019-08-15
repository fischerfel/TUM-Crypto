InputStream keystoreInput = Thread.currentThread().getContextClassLoader()
    .getResourceAsStream(<path in jar>/client.ks");
InputStream truststoreInput = Thread.currentThread().getContextClassLoader()
    .getResourceAsStream(<path in jar>/client.ts");
setSSLFactories(keystoreInput, "password", truststoreInput);
keystoreInput.close();
truststoreInput.close();

private static void setSSLFactories(InputStream keyStream, String keyStorePassword, 
    InputStream trustStream) throws Exception
{    
  // Get keyStore
  KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());    

  // if your store is password protected then declare it (it can be null however)
  char[] keyPassword = keyStorePassword.toCharArray();

  // load the stream to your store
  keyStore.load(keyStream, keyPassword);

  // initialize a trust manager factory with the trusted store
  KeyManagerFactory keyFactory = 
  KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());    
  keyFactory.init(keyStore, keyPassword);

  // get the trust managers from the factory
  KeyManager[] keyManagers = keyFactory.getKeyManagers();

  // Now get trustStore
  KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());    

  // if your store is password protected then declare it (it can be null however)
  //char[] trustPassword = password.toCharArray();

  // load the stream to your store
  trustStore.load(trustStream, null);

  // initialize a trust manager factory with the trusted store
  TrustManagerFactory trustFactory = 
  TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());    
  trustFactory.init(trustStore);

  // get the trust managers from the factory
  TrustManager[] trustManagers = trustFactory.getTrustManagers();

  // initialize an ssl context to use these managers and set as default
  SSLContext sslContext = SSLContext.getInstance("SSL");
  sslContext.init(keyManagers, trustManagers, null);
  SSLContext.setDefault(sslContext);    
}
