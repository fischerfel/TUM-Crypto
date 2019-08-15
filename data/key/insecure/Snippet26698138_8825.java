byte[] keyBytes = new byte[] { 0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, 0x09,
    0x0a, 0x0b, 0x0c, 0x0d, 0x0e, 0x0f, 0x10, 0x11, 0x12, 0x13, 0x14, 0x15, 0x16, 0x17 }; //Choose a key wisely

SecretKeySpec key = new SecretKeySpec(keyBytes, "AES");

Cipher cipher = Cipher.getInstance("AES/ECB/PKCS7Padding", "BC");

fis = new FileInputStream("some_img.png");
cis = new CipherInputStream(fis, cipher);
fos = new FileOutputStream("encrypted_img.enc");
byte[] b = new byte[8];
int i = cis.read(b);
while (i != -1) {
    fos.write(b, 0, i);
    i = cis.read(b);
}
fos.close();
