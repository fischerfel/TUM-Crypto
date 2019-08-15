SecretKey key = new SecretKeySpec(keyBytes, "DESede");
IvParameterSpec iv = new IvParameterSpec(new byte[8]);
Cipher cipher = Cipher.getInstance("DESede/?????/?????"); // transformation spec?
cipher.init(Cipher.ENCRYPT_MODE, key, iv);
byte[] cipherTextBytes = cipher.doFinal(plaintext);
