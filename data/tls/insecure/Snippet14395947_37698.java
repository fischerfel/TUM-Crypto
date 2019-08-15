private static SSLSocketFactory setPrivateKey
  (String keyStoreFileName,
   String keyStoreType,
   String keyStorePassword
  ) 
  throws FileNotFoundException, 
         KeyStoreException, 
         IOException, 
         NoSuchAlgorithmException, 
         CertificateException, 
         UnrecoverableKeyException, 
         KeyManagementException
  {
    // Load the key store: change store type if needed
    KeyStore ks = KeyStore.getInstance(keyStoreType);
    FileInputStream fis = new FileInputStream(keyStoreFileName);
    try {
        ks.load(fis, keyStorePassword.toCharArray());
    } finally {
        if (fis != null) { fis.close(); }
    }
    // Get the default Key Manager
    KeyManagerFactory kmf = KeyManagerFactory.getInstance(
       KeyManagerFactory.getDefaultAlgorithm());   
    kmf.init(ks, keyStorePassword.toCharArray());
     X509KeyManager origKm = (X509KeyManager)kmf.getKeyManagers()[0];
    SSLContext sslContext = SSLContext.getInstance("TLS");
    sslContext.init(new KeyManager[] {origKm }, null, null);

    return sslContext.getSocketFactory();
}
