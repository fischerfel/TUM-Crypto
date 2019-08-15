cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
cipher.init(Cipher.ENCRYPT_MODE, publickey); 
byte[] cipherText = cipher.doFinal(str.getBytes());
port.callX(chiperText.toString());
