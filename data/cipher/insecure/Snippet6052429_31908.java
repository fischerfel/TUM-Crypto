Cipher cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
cipher.init(Cipher.DECRYPT_MODE, serverPublicKey);
CipherInputStream cipherIn = new CipherInputStream(inFromServer, cipher);

byte[] fileBuffer = new byte[BUFFER_SIZE];
FileOutputStream fileWriter = new FileOutputStream(newFileName);
int bytesRead;
while((bytesRead = cipherIn.read(fileBuffer)) != EOF){
    fileWriter.write(fileBuffer, 0, bytesRead);
}
fileWriter.flush();
fileWriter.close();
