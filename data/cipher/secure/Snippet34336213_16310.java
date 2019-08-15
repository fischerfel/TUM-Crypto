public static void main(String[] args) throws Exception {
   byte[] pubKey = Base64.getDecoder().decode(PUBLIC_KEY_BASE64);
   PublicKey publicKey = KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(pubKey));

   Cipher cipher = Cipher.getInstance("RSA");
   Cipher.init(Cipher.ENCRYPT_MODE, publicKey);
   String encryptedData = Base64.getEncoder().encodeToString(cipher.doFinal(args[0].getBytes("UTF-8")));
   System.out.println("Encrypted data is: " + encryptedData);
}
