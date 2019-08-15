byte[] encipheredCodeRandomB = { (byte)0xEA ,(byte)0x18 ,(byte)0xDE ,(byte)0xFF
     ,(byte)0x52 ,(byte)0x0E,(byte)0xCD, (byte) 90};
byte[] masterKeyBytes = "0000000000000000".getBytes();
byte[] ivBytes = "00000000".getBytes();

DESKeySpec desKeySpec = new DESKeySpec(masterKeyBytes);  
SecretKeyFactory desKeyFact = SecretKeyFactory.getInstance("DES");
SecretKey s = desKeyFact.generateSecret(desKeySpec);
aliceCipher = Cipher.getInstance("DES/CBC/NoPadding");
aliceCipher.init(Cipher.DECRYPT_MODE, s, new IvParameterSpec(ivBytes));

byte[] decipheredCodeRandomB = aliceCipher.doFinal(encipheredCodeRandomB);
