String message = request.getHeader("Message"); 
byte [] msgBytes = message.getBytes("UTF-8");

Cipher rsa = Cipher.getInstance("RSA");
rsa.init(Cipher.DECRYPT_MODE, publicKey);
byte [] decryptedMsg = rsa.doFinal(msgBytes);

String decryptedString = new String(decryptedMsg, "UTF-8");
