 Cipher cipher = Cipher.getInstance("AES/CFB128/NoPadding");
 SecretKeySpec keySpec = new SecretKeySpec("1234567890123456".getBytes(), "AES");
 byte[] iv = "1234567890123456".getBytes();
 cipher.init(Cipher.ENCRYPT_MODE, keySpec, new IvParameterSpec(iv));
 Cipher decryptCipher = Cipher.getInstance("AES/CFB128/NoPadding");
 decryptCipher.init(Cipher.DECRYPT_MODE, keySpec, new IvParameterSpec(iv));
 byte[] data = cipher.doFinal("1234567890123456".getBytes());

 for (byte b : data) {
    byte[] output = decryptCipher.update(new byte[]{b});
    System.out.println(new String(output));
 }
