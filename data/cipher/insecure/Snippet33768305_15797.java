String key = "012345678901234567890123";
String clearText = "test";

MessageDigest md = MessageDigest.getInstance("md5");
byte[] digestOfPassword = md.digest(key.getBytes("UTF-16LE"));

byte[] keyBytes = Arrays.copyOf(digestOfPassword, 24);
String byteText = Arrays.toString(keyBytes);

for (int j = 0, k = 16; j < 8;) {
  keyBytes[k++] = keyBytes[j++];
}

SecretKey secretKey = new SecretKeySpec(keyBytes, 0, 24, "DESede");

IvParameterSpec iv = new IvParameterSpec(new byte[8]);
Cipher cipher = Cipher.getInstance("DESede/CBC/PKCS5Padding");

cipher.init(Cipher.ENCRYPT_MODE, secretKey, iv);

byte[] plainTextBytes = clearText.getBytes("UTF-16LE");
byte[] cipherText = cipher.doFinal(plainTextBytes);

String output = Base64.encode(cipherText);
