Cipher pbeCipher = Cipher.getInstance("AES/CTS/NoPadding");
// Initialize cipher
pbeCipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec);
doFinal(data);
