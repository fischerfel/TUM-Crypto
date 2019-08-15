public static void main(String args[]) throws Exception{
  String password = "Secret Passphrase";
  String salt = "4acfedc7dc72a9003a0dd721d7642bde";
  String iv = "69135769514102d0eded589ff874cacd";
  String encrypted = "PU7jfTmkyvD71ZtISKFcUQ==";
  byte[] saltBytes = salt.getBytes(); //hexStringToByteArray(salt);
  byte[] ivBytes = iv.getBytes();//hexStringToByteArray(iv);
  IvParameterSpec ivParameterSpec = new IvParameterSpec(ivBytes);        
  SecretKeySpec sKey = (SecretKeySpec) generateKeyFromPassword(password, saltBytes);
  System.out.println( decrypt( encrypted , sKey ,ivParameterSpec));
}

public static SecretKey generateKeyFromPassword(String password, byte[] saltBytes) throws GeneralSecurityException {

  KeySpec keySpec = new PBEKeySpec(password.toCharArray(), saltBytes, 100, 128/32);
  SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
  SecretKey secretKey = keyFactory.generateSecret(keySpec);
  return new SecretKeySpec(secretKey.getEncoded(), "AES");
}

public static String decrypt(String encryptedData, SecretKeySpec sKey, IvParameterSpec ivParameterSpec) throws Exception {

  Cipher c = Cipher.getInstance("AES");
  c.init(Cipher.DECRYPT_MODE, sKey, ivParameterSpec);
  byte[] decordedValue = new BASE64Decoder().decodeBuffer(encryptedData);
  byte[] decValue = c.doFinal(decordedValue);
  String decryptedValue = new String(decValue);
  return decryptedValue;
}
