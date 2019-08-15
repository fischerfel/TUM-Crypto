Cipher ciph = Cipher.getInstance("AES");

SecretKeySpec AESkeySpec = new SecretKeySpec(keyPass, "AES");
ciph.init(ENCRYPT_MODE,AESkeySpec);
//ciph.update(s.getBytes());

byte[] encryptedData = ciph.doFinal(s.getBytes());
return encryptedData;
