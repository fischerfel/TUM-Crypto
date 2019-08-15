private byte[] getTag(SecretKey key, byte[] nonce, byte[] message,   byte[] associatedData) throws ... {
   Cipher aeadCipher = Cipher.getInstance(AES_GCM_NOPADDING);
   aeadCipher.init(Cipher.ENCRYPT_MODE, kint, new GCMParameterSpec(GCM_MAC_SIZE, nonce);
   aeadCipher.updateAAD(associatedData);
   byte[] encrypted = aeadCipher.doFinal(message);
   // Assuming you have an AAD_SIZE = 128 bits (16 bytes)
   return Arrays.copyOfRange (encrypted, encrypted.length-16, encrypted.length)
