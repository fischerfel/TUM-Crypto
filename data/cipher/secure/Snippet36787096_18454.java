//Get public key from cert
RSAPublicKey eServer = (RSAPublicKey) cert.getPublicKey();

//Encrypt with RSA as key generation algorithm 
Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
cipher.init(Cipher.ENCRYPT_MODE, eServer);
byte[] cipherText = cipher.doFinal(message.getBytes());
System.out.println("Ciphertext: " + cipherText);
os.writeObject(cipherText);
os.flush();
os.close();
s.close();
input.close();
