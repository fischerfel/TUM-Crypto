byte[] buffer = new byte[4096];
FileInputStream fileInStream = new FileInputStream("in.txt");
FileOutputStream fileStream = new FileOutputStream("test.bin");
Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
CipherOutputStream cipherStream = new CipherOutputStream(fileStream, cipher);

while(fileInStream.read(buffer) > 0){
    cipherStream.write(buffer);
}
