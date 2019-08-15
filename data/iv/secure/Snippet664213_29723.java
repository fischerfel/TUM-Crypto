byte[] sessionKey = null; //Where you get this from is beyond the scope of this post
byte[] iv = null ; //Ditto
byte[] plaintext = null; //Whatever you want to encrypt/decrypt
Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
//You can use ENCRYPT_MODE or DECRYPT_MODE
cipher.calling init(Cipher.ENCRYPT_MODE, new SecretKeySpec(sessionKey, "AES"), new IvParameterSpec(iv));
byte[] ciphertext = cipher.doFinal(plaintext);
