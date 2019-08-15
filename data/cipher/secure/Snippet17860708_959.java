String data = "... data to be encrypted ....";
String alg = "RSA/ECB/PKCS1Padding";
Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
cipher.init(Cipher.ENCRYPT_MODE, publicKey);
byte encryptedBytes[] = cipher.doFinal(data.getBytes());
