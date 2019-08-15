 KeyGenerator kg = KeyGenerator.getInstance("AES");
kg.init(128);
SecretKey key = kg.generateKey();
 Cipher c = Cipher.getInstance("AES");
c.init(Cipher.ENCRYPT_MODE, key);
FileInputStream fis;    FileOutputStream fos;    CipherOutputStream cos;
fis = new FileInputStream("FileTo.encrypt");
fos = new FileOutputStream("Encrypted.file");

//write encrypted to file
cos = new CipherOutputStream(fos, c);
byte[] b = new byte[16];
int i = fis.read(b);
while (i != -1) {
    cos.write(b, 0, i);
    i = fis.read(b);
}
cos.close();

 //write key to file
 byte[] keyEncoded = key.getEncoded();    
 FileOutputStream kos = new FileOutputStream("crypt.key");
 kos.write(keyEncoded);
 kos.close();
