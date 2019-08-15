System.out.println("Base 64: " + encodedChallenge.length()); //1248 long
byte[] base64Message = encodedChallenge.getBytes();
byte[] rsaEncodedMessage = Base64.decode(base64Message);
System.out.println("RSA: " + rsaEncodedMessage.length); //936 long
cipher = Cipher.getInstance("RSA/NONE/OAEPWithSHA256AndMGF1Padding");
cipher.init(Cipher.DECRYPT_MODE, privateKey);
cipherData = cipher.doFinal(rsaEncodedMessage); //hangs up
System.out.println("Ciper: " + new String(cipherData));
