SecretKey secret = new SecretKeySpec(aesKey, "AES");
Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
cipher.init(Cipher.ENCRYPT_MODE, secret);
// everything else as before
