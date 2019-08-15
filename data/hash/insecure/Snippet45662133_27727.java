public static String EncryptText(String message) throws Exception {

MessageDigest md = MessageDigest.getInstance("SHA-1");
byte[] digestOfPassword = md.digest(secretKey.getBytes("utf-8"));
byte[] keyBytes = Arrays.copyOf(digestOfPassword, 24);

SecretKey key = new SecretKeySpec(keyBytes, "DESede");
Cipher cipher = Cipher.getInstance("DESede");
cipher.init(Cipher.ENCRYPT_MODE, key);

byte[] plainTextBytes = message.getBytes("utf-8");
byte[] buf = cipher.doFinal(plainTextBytes);
byte [] base64Bytes = Base64.encode(buf,Base64.DEFAULT);
String base64EncryptedString = new String(base64Bytes);

return base64EncryptedString;
