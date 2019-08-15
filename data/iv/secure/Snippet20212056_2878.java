byte[] plaintext = ...;
byte[] key = ...;

// get iv
SecureRandom rnd = new SecureRandom();
byte[] iv = rnd.getBytes(16);
IvParameterSpec ivSpec = new IvParameterSpec(iv);   

// encrypt
Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding");
cipher.init(Cipher.ENCRYPT_MODE, key, ivSpec);
byte[] ciphertext = cipher.doFinal(plaintext);

// copy to result
byte[] result = new byte[iv.length + ciphertext.length];
System.arraycopy(iv, 0, result, 0, iv.length);
System.arraycopy(ciphertext, 0 , result, iv.length, ciphertext.length);
