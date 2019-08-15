 public class AESImpl {

  private static String decryptedString;

  private static String encryptedString;

  public static void main(String[] args) throws NoSuchAlgorithmException, IOException,  ClassNotFoundException {

    String strToEncrypt = "This text has to be encrypted";
    SecretKey secretKey = generateSecretKey();
    String encryptStr = encrypt(strToEncrypt, secretKey);
    System.out.println("Encrypted String : " + encryptStr + "It should not come in new line");
    String decryptStr = decrypt(encryptStr, secretKey);
    System.out.println("Decrypted String : " + decryptStr);
  }

  private static SecretKey generateSecretKey() throws NoSuchAlgorithmException, IOException {
    KeyGenerator kg = KeyGenerator.getInstance("AES");
    kg.init(128);
    SecretKey sk = kg.generateKey();
    String secretKey = String.valueOf(Hex.encodeHex(sk.getEncoded()));
    System.out.println("Secret key is " + secretKey);
    return sk;
  }

  public static String encrypt(String strToEncrypt, SecretKey secretKey) {
    try {
      Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5PADDING");
      cipher.init(Cipher.ENCRYPT_MODE, secretKey);
      encryptedString = new String(Base64.encodeBase64String(cipher.doFinal(strToEncrypt.getBytes())));
    } catch (Exception e) {
      System.out.println("Error while encrypting: " + e.toString());
    }

    return encryptedString;
  }

  public static String decrypt(String strToDecrypt, SecretKey secretKey) {
    try {
      Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5PADDING");
      cipher.init(Cipher.DECRYPT_MODE, secretKey);
      decryptedString = new String(cipher.doFinal(Base64.decodeBase64(strToDecrypt)));
    } catch (Exception e) {
      System.out.println("Error while decrypting: " + e.toString());
    }

    return decryptedString;
  }
}
