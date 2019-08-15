//Decrypt: server's private key 
Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
byte[] in = (byte[]) is.readObject();
cipher.init(Cipher.DECRYPT_MODE, dServer);
byte[] plaintText = cipher.doFinal(in);
System.out.println("The plaintext is: " + new String(plaintText));
server.close();
