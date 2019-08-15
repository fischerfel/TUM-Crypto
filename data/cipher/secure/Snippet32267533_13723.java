// Initialise the RSA cipher with PUBLIC key
Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
cipher.init(Cipher.ENCRYPT_MODE, keyPair.getPublic());
