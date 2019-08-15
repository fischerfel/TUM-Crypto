SecretKey AESkey = new SecretKeySpec(
  byteKey, 0, byteKey.length, "AES/CBC/PKCS5Padding");  

if (ks == null) 
{
  ks = KeyStore.getInstance("AndroidKeyStore");
  ks.load(null);
}

ks.deleteEntry("aes_key");
ks.setEntry("aes_key",
   new KeyStore.SecretKeyEntry(AESkey),
   new KeyProtection.Builder(KeyProperties.PURPOSE_ENCRYPT |
         KeyProperties.PURPOSE_DECRYPT)
         .setBlockModes(KeyProperties.BLOCK_MODE_CBC)
         .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_PKCS7)
         .build());
