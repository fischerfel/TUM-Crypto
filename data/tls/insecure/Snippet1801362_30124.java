 /**
  * Returns an array of KeyManagers, set up to use the required keyStore.
  * This method does the bulk of the work of setting up the custom trust managers.
  * 
  * @param props 
  * 
  * @return an array of KeyManagers set up accordingly.
  */
 private static KeyManager[] getKeyManagers(Properties props) throws IOException, GeneralSecurityException {
  // First, get the default KeyManagerFactory.
  String alg = KeyManagerFactory.getDefaultAlgorithm();
  KeyManagerFactory kmFact = KeyManagerFactory.getInstance(alg);   
  // Next, set up the KeyStore to use. We need to load the file into
  // a KeyStore instance.
  FileInputStream fis = new FileInputStream(props.getProperty(SSL_KEYSTORE));
  logger.info("Loaded keystore");
  KeyStore ks = KeyStore.getInstance("jks");
  String keyStorePassword = props.getProperty(SSL_KEYSTORE_PASSWORD);
  ks.load(fis, keyStorePassword.toCharArray());
  fis.close();
  // Now we initialise the KeyManagerFactory with this KeyStore
  kmFact.init(ks, keyStorePassword.toCharArray());

  // default
  KeyManagerFactory dkmFact = KeyManagerFactory.getInstance(alg); 
  dkmFact.init(null,null);  

  // Get the first X509KeyManager in the list
  X509KeyManager customX509KeyManager = getX509KeyManager(alg, kmFact);
  X509KeyManager jvmX509KeyManager = getX509KeyManager(alg, dkmFact);

  KeyManager[] km = { new MultiKeyStoreManager(jvmX509KeyManager, customX509KeyManager) };   
  logger.debug("Number of key managers registered:" + km.length);  
  return km;
 }


 /**
  * Find a X509 Key Manager compatible with a particular algorithm
  * @param algorithm
  * @param kmFact
  * @return
  * @throws NoSuchAlgorithmException
  */
 private static X509KeyManager getX509KeyManager(String algorithm, KeyManagerFactory kmFact)
   throws NoSuchAlgorithmException {
  KeyManager[] keyManagers = kmFact.getKeyManagers();

  if (keyManagers == null || keyManagers.length == 0) {
   throw new NoSuchAlgorithmException("The default algorithm :" + algorithm + " produced no key managers");
  }

  X509KeyManager x509KeyManager = null;

  for (int i = 0; i < keyManagers.length; i++) {
   if (keyManagers[i] instanceof X509KeyManager) {
    x509KeyManager = (X509KeyManager) keyManagers[i];
    break;
   }
  }

  if (x509KeyManager == null) {
   throw new NoSuchAlgorithmException("The default algorithm :"+ algorithm + " did not produce a X509 Key manager");
  }
  return x509KeyManager;
 }




 private static void initialiseManager(Properties props) throws IOException, GeneralSecurityException { 
  // Next construct and initialise a SSLContext with the KeyStore and
  // the TrustStore. We use the default SecureRandom.
  SSLContext context = SSLContext.getInstance("SSL");
  context.init(getKeyManagers(props), getTrustManagers(props), null);
  SSLContext.setDefault(context);

 }
