InputStream is = item.openStream(); // item is obtained from file upload iterator
try{
   PBEKeySpec keySpec = new PBEKeySpec(passphrase.toCharArray(), salt.getBytes(),  iterations, keyLength);
   SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DESede"); 
   SecretKey key = keyFactory.generateSecret(keySpec); // Throws Exception
   CipherInputStream cis;
   Cipher cipher = Cipher.getInstance("RSA");
   cis = new CipherInputStream(is, cipher);
   cipher.init(Cipher.ENCRYPT_MODE, key);
 } catch (Exception ex){
    // catches the following exceptopn 
    java.security.spec.InvalidKeySpecException: Inappropriate key specification
    at com.sun.crypto.provider.DESedeKeyFactory.engineGenerateSecret(DashoA13*..)
   //
 }
