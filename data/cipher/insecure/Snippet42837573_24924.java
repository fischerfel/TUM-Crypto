 for encryption

{

// create a cipher using a key to initialize it
Cipher cipher = Cipher.getInstance("DESede/CBC/NoPadding");
cipher.init(Cipher.ENCRYPT_MODE, key, iv);

// perform the actual encryption
byte[] ciphertext = cipher.doFinal(text);

return ciphertext;
}

   for decryption 
 {
// create a cipher using a key to initialize it
Cipher cipher = Cipher.getInstance("DESede/CBC/NoPadding");
cipher.init(Cipher.DECRYPT_MODE, key, iv);

// perform the decryption
byte[] decryptedText = cipher.doFinal(text);

return decryptedText;
}
