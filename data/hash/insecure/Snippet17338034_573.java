public static String signData(String dataToSign, String keyFile) { 
  FileInputStream keyfis = null;
  try {
    keyfis = new FileInputStream(fileName);
    KeyStore store = KeyStore.getInstance("PKCS12");
    store.load(keyfis, "TestPassword".toCharArray());
    KeyStore.PrivateKeyEntry pvk = (KeyStore.PrivateKeyEntry)store.
          getEntry("testkey", 
          new KeyStore.PasswordProtection("TestPassword".toCharArray()));
    PrivateKey privateKey = (PrivateKey)pvk.getPrivateKey();
    byte[] data = dataToSign.getBytes("US-ASCII");
    MessageDigest md = MessageDigest.getInstance("SHA1");
    byte[] hashed = md.digest(data);
    Signature rsa = Signature.getInstance("SHA1withRSA");
    rsa.initSign(privateKey);
    rsa.update(data);
    return Base64.encode(rsa.sign());
  } catch (Exception ex) {
    Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
  } finally {
    if ( keyfis != null ) {
      try { keyfis.close() } catch (Exception ex) { keyfis = null; }
    }
  }
  return null;
}
