Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
byte[] iv = cipher.getIV(); // randomly filled.
...

// on decrypt specify this IV again
cipher.init(Cipher.DECRYPT_MODE, skeySpec, new IvParameterSpec(iv));
