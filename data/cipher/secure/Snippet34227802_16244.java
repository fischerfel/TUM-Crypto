// 1. Get the cipher ready to start doing the RSA transformation
Cipher cipher = Cipher.getInstance("RSA");

// 2. Start the decryption process
cipher.init(Cipher.DECRYPT_MODE, privateKey);
