Security.addProvider(new BouncyCastleProvider());
SecretKeySpec keySpec = new SecretKeySpec(KEY.getBytes(), "DESede");
IvParameterSpec iv = new IvParameterSpec(new byte[8]);
Cipher e_cipher = Cipher.getInstance("DESede/CBC/PKCS7Padding", "BC");
e_cipher.init(Cipher.DECRYPT_MODE, keySpec, iv);

byte[] hexdecoded = Hex.decode(ENCRYPTED.getBytes());
byte [] cipherText = e_cipher.doFinal(hexdecoded);

return new String(cipherText);
