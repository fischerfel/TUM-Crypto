Cipher cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
cipher.init(Cipher.ENCRYPT_MODE, publicKey);
CipherOutputStream cipherOut = new CipherOutputStream(outToClient, cipher);
byte[] fileBuffer = new byte[BUFFER_SIZE];
InputStream fileReader = new BufferedInputStream(new FileInputStream(aFile));
int bytesRead;
while((bytesRead = fileReader.read(fileBuffer)) != EOF){
    cipherOut.write(fileBuffer, 0, bytesRead);
}
cipherOut.flush();
