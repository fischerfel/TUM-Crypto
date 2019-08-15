s = new Socket(serverAddress, serverPort);
is = s.getInputStream();
os = s.getOutputStream();

Cipher decryptCipher = Cipher.getInstance("RSA");
decryptCipher.init(Cipher.DECRYPT_MODE, ClientSocket.clientPrivateKey);
cis = new CipherInputStream(is,decryptCipher);

Cipher encryptCipher = Cipher.getInstance("RSA");
encryptCipher.init(Cipher.ENCRYPT_MODE, this.serverPublicKey);
cos = new CipherOutputStream(os,encryptCipher);
