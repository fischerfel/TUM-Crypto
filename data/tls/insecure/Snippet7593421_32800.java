// load your key store as a stream and initialize a KeyStore
InputStream trustStream = ...    
KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());    

// if your store is password protected then declare it (it can be null however)
char[] trustPassword = ...

// load the stream to your store
trustStore.load(trustStream, trustPassword);

// initialize a trust manager factory with the trusted store
TrustManagerFactory trustFactory = 
  TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());    
trustFactory.init(trustStore);

// get the trust managers from the factory
TrustManager[] trustManagers = trustFactory.getTrustManagers();

// initialize an ssl context to use these managers and set as default
SSLContext sslContext = SSLContext.getInstance("SSL");
sslContext.init(null, trustManagers, null);
SSLContext.setDefault(sslContext);
