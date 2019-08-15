byte [] PRFkey = {0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15};
byte [] plaintext = {0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15};

SecretKeySpec encryptionKey=new SecretKeySpec(PRFkey, "AES");
Cipher cipher=Cipher.getInstance("AES");
cipher.init(Cipher.ENCRYPT_MODE, encryptionKey);
byte[] encryptedData=cipher.doFinal(plaintext);
