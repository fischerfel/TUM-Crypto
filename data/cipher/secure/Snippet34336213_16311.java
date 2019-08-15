public static void main(String[] args) throws Exception {
  byte[] privKey = Base64.getDecoder().decode(PRIVATE_KEY_BASE64);
  PrivateKey privateKey = KeyFactory.getInstance("RSA").generatePrivate(new PKCS8EncodedKeySpec(privKey));

  Cipher cipher = Cipher.getInstance("RSA");
  cipher.init(Cipher.DECRYPT_MODE, privateKey);
  byte[] encryptedData = Base64.getDecoder().decode(args[0]); 
  byte[] decryptedData = cipher.doFinal(encryptedData);
  System.out.println("Decrypted message was: " + new String(decryptedData, "UTF-8"));

}
