 din = new DataInputStream(socket.getInputStream());
 dout = new DataOutputStream(socket.getOutputStream());

 Cipher cipher = Cipher.getInstance("RSA"); 
 // encrypt the aeskey using the public key 
 cipher.init(Cipher.ENCRYPT_MODE, pk);

 byte[] cipherText = cipher.doFinal(aesKey.getEncoded());
 dout.write(cipherText);
