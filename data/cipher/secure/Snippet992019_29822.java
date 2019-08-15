byte[] key = null; // TODO
byte[] input = null; // TODO
byte[] output = null;
SecretKeySpec keySpec = null;
keySpec = new SecretKeySpec(key, "AES");
Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding");
cipher.init(Cipher.ENCRYPT_MODE, keySpec);
output = cipher.doFinal(input)
