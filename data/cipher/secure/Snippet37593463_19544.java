/* Decrypt the message, given derived key and initialization vector. */
Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
cipher.init(Cipher.DECRYPT_MODE, secret, new IvParameterSpec(iv));
String plaintext = new String(cipher.doFinal(ciphertext), "UTF-8");
System.out.println(plaintext);
