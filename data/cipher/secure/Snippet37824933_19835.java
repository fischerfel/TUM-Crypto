int bytesRead;
int current = 0;
FileOutputStream fos = null;
byte[] EncryptedClientAES = new byte[10000];
InputStream is = clientSocket.getInputStream();
fos = new FileOutputStream("ClientAESFile");
BufferedOutputStream bos = new BufferedOutputStream(fos);
bytesRead = is.read(EncryptedClientAES, 0, EncryptedClientAES.length);
current = bytesRead;
System.out.println("Size of read data: " + current);

bos.write(EncryptedClientAES, 0, current);

bos.flush();
bos.close();
fos.close();
clientSocket.close();

Cipher cipher = Cipher.getInstance("RSA");
cipher.init(Cipher.DECRYPT_MODE, privateKey);
byte[] decryptedAESKeyOfClient = null;
decryptedAESKeyOfClient = cipher.doFinal(EncryptedClientAES);
