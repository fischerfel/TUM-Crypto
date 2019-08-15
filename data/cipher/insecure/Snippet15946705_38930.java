byte[] userPin3DESEncrypted = Base64.decodeBase64(userPin3DESBase64Encrypted.getBytes());
byte [] keyByte = "jgd8f3m8ybjhwlGhr4hihbp0".getBytes();
SecretKeySpec secretKeySpec = new SecretKeySpec(keyByte, "DESEDE");
Cipher cipher = Cipher.getInstance("DESEDE/ECB/PKCS5Padding");
cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);
String userPinDecrypted = new String(cipher.doFinal(userPin3DESEncrypted));
