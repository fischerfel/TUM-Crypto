public class AppSecurity{
private AppSecurity() {}

public static byte[] encrypt(byte[] key , byte[] data) {
    SecretKeySpec keySpec = new SecretKeySpec(key, "AES");
    try {
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE,keySpec);
        return cipher.doFinal(data);
    }
    catch (NoSuchAlgorithmException e){ }
    catch (NoSuchPaddingException e){ }
    catch (InvalidKeyException e){ }
    catch (BadPaddingException e){ }
    catch (IllegalBlockSizeException e) {}
    return null;
}

public static byte[] decrypt(byte[] key , byte[] encryptedData) {
    SecretKeySpec keySpec = new SecretKeySpec(key ,"AES");
    try {
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, keySpec);
        return cipher.doFinal(encryptedData);
    }
    catch (NoSuchAlgorithmException e) {}
    catch (NoSuchPaddingException e) { }
    catch (InvalidKeyException e) { }
    catch (BadPaddingException e) {}
    catch (IllegalBlockSizeException e) {}
    return null;
}

/**
 * method to generate a secure key. call this when app starts
 * @return
 */
public static byte[] generateKey(){
    try{
        // create an AES algorithm instance.
        KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
        SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
        keyGenerator.init(128,secureRandom);
        SecretKey secretKey = keyGenerator.generateKey();
        return secretKey.getEncoded();
    }
    catch (NoSuchAlgorithmException e){
        return null;
    }
  }
}
