Cipher cipher = Cipher.getInstance("Blowfish");
cipher.init(Cipher.ENCRYPT_MODE, key);
FileInputStream fis = new FileInputStream(plain);

FileOutputStream fos = new FileOutputStream(copy);
CipherOutputStream out2 = new CipherOutputStream(fos, cipher);
byte[] buffer = new byte[1024];
while (fis.read(buffer)>=0) {
    out2.write(buffer);
}
