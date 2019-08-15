String[] fields = ciphertext.split("]");
byte[] salt = fromBase64(fields[0]);
byte[] iv = fromBase64(fields[1]);
byte[] cipherBytes = fromBase64(fields[2]);
// as above
SecretKey key = deriveKeyPbkdf2(salt, password);

Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
IvParameterSpec ivParams = new IvParameterSpec(iv);
cipher.init(Cipher.DECRYPT_MODE, key, ivParams);
byte[] plaintext = cipher.doFinal(cipherBytes);
String plainrStr = new String(plaintext , "UTF-8");
