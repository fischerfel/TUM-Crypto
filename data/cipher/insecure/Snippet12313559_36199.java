DESKeySpec desKeySpec = new DESKeySpec(masterKeyBytes);  
SecretKeyFactory desKeyFact = SecretKeyFactory.getInstance("DES");
SecretKey s = desKeyFact.generateSecret(desKeySpec);
dfCardCipher = Cipher.getInstance("DES/CBC/NoPadding");
dfCardCipher.init(Cipher.DECRYPT_MODE, s, new IvParameterSpec(ivBytes));

byte[] decipheredCodeRandomB = dfCardCipher.doFinal(encipheredCodeRandomB);
