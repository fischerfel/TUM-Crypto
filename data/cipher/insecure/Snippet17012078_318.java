 byte[] encryptedByteArray;
Cipher eCipher=Cipher.getInstance("AES");
eCipher.init(Cipher.ENCRYPT_MODE,key );
encryptedByteArray=eCipher.doFinal(clearByteArray);
