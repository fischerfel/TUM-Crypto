  private static byte[] encryptDecrypt(boolean encrypt, byte[] iv, String salt, String password, byte[] bytesToEncryptDecrypt) throws Exception {
SecretKeySpec secretKeySpec = null;
MessageDigest digester = MessageDigest.getInstance("SHA-1");
digester.update((salt + password).getBytes("UTF-8"));
byte[] key = digester.digest();
secretKeySpec = new SecretKeySpec(key, 2, 16, "AES");
IvParameterSpec ivps = new IvParameterSpec(iv);
Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
cipher.init(encrypt ? 1 : 2, secretKeySpec, ivps);
return cipher.doFinal(bytesToEncryptDecrypt, 0, bytesToEncryptDecrypt.length);
