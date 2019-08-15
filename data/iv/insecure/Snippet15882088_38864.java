byte[] ivdata = new byte[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
 IvParameterSpec spec = new IvParameterSpec(ivdata);
 symetricKeyFromCard = new SecretKeySpec(symKeyData, "AES");

 Cipher cipherAes = Cipher.getInstance("AES/CBC/NoPadding");
 cipherAes.init(Cipher.DECRYPT_MODE, symetricKeyFromCard, spec);
 byte[] decryptedBytes = cipherAes.doFinal(challengeEncrypted);
