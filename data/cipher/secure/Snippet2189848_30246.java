public Encrypt(SecretKey key, String algorithm) {

 try {
     ecipher = Cipher.getInstance(algorithm);
     dcipher = Cipher.getInstance(algorithm);
     ecipher.init(Cipher.ENCRYPT_MODE, key);
     dcipher.init(Cipher.DECRYPT_MODE, key);
 } catch (NoSuchPaddingException e) {
     System.out.println("EXCEPTION: NoSuchPaddingException");
 } catch (NoSuchAlgorithmException e) {
     System.out.println("EXCEPTION: NoSuchAlgorithmException");
 } catch (InvalidKeyException e) {
     System.out.println("EXCEPTION: InvalidKeyException");
 }
}

public void useSecretKey(String secretString) {


 try {
     SecretKey desKey       = KeyGenerator.getInstance("DES").generateKey();
     SecretKey blowfishKey  = KeyGenerator.getInstance("Blowfish").generateKey();
     SecretKey desedeKey    = KeyGenerator.getInstance("DESede").generateKey();

     Encrypt desEncrypter = new Encrypt(desKey, desKey.getAlgorithm());
     Encrypt blowfishEncrypter = new Encrypt(blowfishKey, blowfishKey.getAlgorithm());
     Encrypt desedeEncrypter = new Encrypt(desedeKey, desedeKey.getAlgorithm());

     desEncrypted       = desEncrypter.encrypt(secretString);
     blowfishEncrypted  = blowfishEncrypter.encrypt(secretString);
     desedeEncrypted    = desedeEncrypter.encrypt(secretString);
 } catch (NoSuchAlgorithmException e) {}
}
