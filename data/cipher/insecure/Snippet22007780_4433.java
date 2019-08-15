BufferedReader reader = new BufferedReader(new FileReader(keylocation.getFile()));
String line = null;
StringBuilder stringBuilder = new StringBuilder();
while ((line = reader.readLine()) != null) {
  stringBuilder.append(line);
}
String secretKey = stringBuilder.toString();
SecretKeySpec key = new SecretKeySpec(secretKey.getBytes("UTF-8"), "AES");

Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
cipher.init(Cipher.ENCRYPT_MODE, key);
encryptedString = (Base64.encodeBase64String(cipher.doFinal(strToEncrypt.getBytes("UTF-8"))));
