FileInputStream fis = new FileInputStream("/sdcard/encrypted.apk");

FileOutputStream fos = new FileOutputStream("/sdcard/decrypted.apk");
SecretKeySpec sks = new SecretKeySpec("MyDifficultPassw".getBytes(), "AES");
Cipher cipher = Cipher.getInstance("AES");
cipher.init(Cipher.DECRYPT_MODE, sks);
CipherInputStream cis = new CipherInputStream(fis, cipher);
int b;
byte[] d = new byte[8];
while((b = cis.read(d)) != -1) {
    fos.write(d, 0, b);
}
fos.flush();
fos.close();
cis.close();
