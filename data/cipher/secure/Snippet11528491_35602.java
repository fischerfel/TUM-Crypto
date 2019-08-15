Cipher keyEncryptionCipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
for (Certificate recipient : recipients) {
  keyEncryptionCipher.init(Cipher.WRAP_MODE, recipient);
  byte[] encryptedKey = keyEncryptionCipher.wrap(contentEncryptionKey);
  /* Store the encryptedKey with an identifier for the recipient... */
}
/* Store the IV... */ 
/* Encrypt the file... */
