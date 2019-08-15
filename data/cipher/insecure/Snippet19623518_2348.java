Cipher desCipher
// Create the cipher 
desCipher = Cipher.getInstance("DES/ECB/NoPadding");
desCipher.init(Cipher.ENCRYPT_MODE, myDesKey);
byte[] textEncrypted = desCipher.doFinal(msgBytes);
