byte[] ciphertext;

Cipher enc = Cipher.getInstance("DES/CBC/PKCS5Padding");   

enc.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(key, "DES"), new IvParameterSpec(vector));

// Is this the complete ciphertext?
ciphertext = encrypt.doFinal(data.getbytes("UTF-8"));
