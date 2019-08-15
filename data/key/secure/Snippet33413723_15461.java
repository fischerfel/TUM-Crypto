Cipher encryptCipher;
IvParameterSpec iv = new IvParameterSpec(key);
SecretKeySpec skeySpec = new SecretKeySpec(key, "AES");
encryptCipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
encryptCipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);
byte[] encrypted = encryptCipher.doFinal(dataToEncrypt.getBytes());
Log.d("TAG", "encrypted string:"
        + Base64.encodeToString(encrypted, Base64.DEFAULT));
return Base64.encodeToString(encrypted, Base64.DEFAULT).trim();
