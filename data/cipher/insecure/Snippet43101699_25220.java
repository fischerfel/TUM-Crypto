public class AES {

  public static void main(String ... args) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, UnsupportedEncodingException {
    final String Algo="AES";
    String key = "aaaaaaaaaaaaaaaa";
    byte[] keyBytes = key.getBytes(StandardCharsets.UTF_8);

    MessageDigest sha= MessageDigest.getInstance("SHA-1"); 
    keyBytes=sha.digest(keyBytes);
    keyBytes=Arrays.copyOf(keyBytes, 16);

    SecretKeySpec secretKeySpec = new SecretKeySpec(keyBytes, Algo);
    Cipher cipher = Cipher.getInstance(Algo);
    cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
    byte[] ciphertext = cipher.doFinal("Message".getBytes());
    System.out.println("Encrypted Message: " +new String(ciphertext));

    cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);
    byte[] original = cipher.doFinal(ciphertext);
    String originalString = new String(original);
    System.out.println("keybyte: "+keyBytes);
    System.out.println("Original string: " + originalString + "\nOriginal string (Hex): " +original);      
  }
}
