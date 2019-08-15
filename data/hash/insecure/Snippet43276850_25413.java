 public String _encrypt(String message) throws Exception {
 MessageDigest md = MessageDigest.getInstance("SHA-1");
 byte[] digestOfPassword = md.digest(secretKey.getBytes("utf-8"));
 byte[] keyBytes = Arrays.copyOf(digestOfPassword, 24);
 System.out.println(bytesToHex(keyBytes));

 SecretKey key = new SecretKeySpec(keyBytes, "TripleDES");
 Cipher cipher = Cipher.getInstance("TripleDES");
 cipher.init(Cipher.ENCRYPT_MODE,key);
 byte[] plainTextBytes = message.getBytes("utf-8");
 byte[] buf = cipher.doFinal(plainTextBytes);

 System.out.println(bytesToHex(buf));



 byte [] base64Bytes = Base64.encodeBase64(buf);
 String base64EncryptedString = new String(base64Bytes);

 return base64EncryptedString;
}

 public static String bytesToHex(byte[] in) {
 final StringBuilder builder = new StringBuilder();
 for(byte b : in) {
     builder.append(String.format("%02x", b));
 }
 return builder.toString();
 }


 public String _decrypt(String encryptedText) throws Exception {

 byte[] message = Base64.decodeBase64(encryptedText.getBytes("utf-8"));

MessageDigest md = MessageDigest.getInstance("SHA-1");
byte[] digestOfPassword = md.digest(secretKey.getBytes("utf-8"));
byte[] keyBytes = Arrays.copyOf(digestOfPassword, 24);
SecretKey key = new SecretKeySpec(keyBytes, "DESede");

Cipher decipher = Cipher.getInstance("DESede");
decipher.init(Cipher.DECRYPT_MODE, key);

byte[] plainText = decipher.doFinal(message);

return new String(plainText, "UTF-8");
}
