public byte[] encrypt(String plainText) throws Exception {
   byte[] iv = new byte[cipher.getBlockSize()];    
   AlgorithmParameterSpec spec = new IvParameterSpec(iv);
   cipher.init(Cipher.ENCRYPT_MODE, key, spec);
   return cipher.doFinal(plainText.getBytes());
}

public String decrypt(byte[] cryptedText) throws Exception {
   byte[] iv = new byte[cipher.getBlockSize()];
   AlgorithmParameterSpec spec = new IvParameterSpec(iv);
   cipher.init(Cipher.DECRYPT_MODE, key, spec);
   // decrypt the message
   byte[] decrypted = cipher.doFinal(cryptedText);
   decryptedText = new String(decrypted, "UTF-8");
   return decryptedText;
}



String decryptedText = aes.decrypt(aes.encrypt(message)).toString();     
