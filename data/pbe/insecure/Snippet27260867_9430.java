public static void main(String args[]) throws Exception{

 String password = "Secret Passphrase";
 String salt = "222f51f42e744981cf7ce4240eeffc3a";
 String iv = "2b69947b95f3a4bb422d1475b7dc90ea";
 String encrypted = "CQVXTPM2ecOuZk+9Oy7OyGJ1M6d9rW2D/00Bzn9lkkehNra65nRZUkiCgA3qlpzL";

 byte[] saltBytes = hexStringToByteArray(salt);
 byte[] ivBytes = hexStringToByteArray(iv);
 IvParameterSpec ivParameterSpec = new IvParameterSpec(ivBytes);        
 SecretKeySpec sKey = (SecretKeySpec) generateKeyFromPassword(password, saltBytes);
 System.out.println( decrypt( encrypted , sKey ,ivParameterSpec));

}

public static SecretKey generateKeyFromPassword(String password, byte[] saltBytes) throws GeneralSecurityException {

 KeySpec keySpec = new PBEKeySpec(password.toCharArray(), saltBytes, 100, 128);
 SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
 SecretKey secretKey = keyFactory.generateSecret(keySpec);

 return new SecretKeySpec(secretKey.getEncoded(), "AES");
}

public static byte[] hexStringToByteArray(String s) {

 int len = s.length();
 byte[] data = new byte[len / 2];

 for (int i = 0; i < len; i += 2) {
    data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
    + Character.digit(s.charAt(i+1), 16));
 }

  return data;

}

public static String decrypt(String encryptedData, SecretKeySpec sKey, IvParameterSpec ivParameterSpec) throws Exception { 

 Cipher c = Cipher.getInstance("AES/CBC/PKCS5Padding");
 c.init(Cipher.DECRYPT_MODE, sKey, ivParameterSpec);
 byte[] decordedValue = new BASE64Decoder().decodeBuffer(encryptedData);
 byte[] decValue = c.doFinal(decordedValue);
 String decryptedValue = new String(decValue);

 return decryptedValue;
}
