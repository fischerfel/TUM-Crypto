String secret = "René Über";
String cipherText = "U2FsdGVkX1+tsmZvCEFa/iGeSA0K7gvgs9KXeZKwbCDNCs2zPo+BXjvKYLrJutMK+hxTwl/hyaQLOaD7LLIRo2I5fyeRMPnroo6k8N9uwKk=";

byte[] cipherData = Base64.getDecoder().decode(cipherText);
byte[] saltData = Arrays.copyOfRange(cipherData, 8, 16);

MessageDigest md5 = MessageDigest.getInstance("MD5");
final byte[][] keyAndIV = GenerateKeyAndIV(32, 16, 1, saltData, secret.getBytes(StandardCharsets.UTF_8), md5);
SecretKeySpec key = new SecretKeySpec(keyAndIV[0], "AES");
IvParameterSpec iv = new IvParameterSpec(keyAndIV[1]);

byte[] encrypted = Arrays.copyOfRange(cipherData, 16, cipherData.length);
Cipher aesCBC = Cipher.getInstance("AES/CBC/PKCS5Padding");
aesCBC.init(Cipher.DECRYPT_MODE, key, iv);
byte[] decryptedData = aesCBC.doFinal(encrypted);
String decryptedText = new String(decryptedData, StandardCharsets.UTF_8);

System.out.println(decryptedText);
