// get the key
final KeyGenerator generator = KeyGenerator.getInstance("AES");
generator.init(128);
final SecretKey secretKey = generator.generateKey();

// perform encryption
final Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
cipher.init(Cipher.ENCRYPT_MODE, secretKey);
FileInputStream fis = new FileInputStream(System.getProperty("user.home") + java.io.File.separatorChar + "plain.pdf");
FileOutputStream fos = new FileOutputStream(System.getProperty("user.home") + java.io.File.separatorChar + "test.enc");
final CipherOutputStream output = new CipherOutputStream(fos, cipher);

int bytesRead = 0;
final byte[] plainText = new byte[4096];
while ((bytesRead = fis.read(plainText)) >= 0) {
    output.write(plainText, 0, bytesRead);
}
output.flush();
output.close();
fos.close();
fis.close();
final byte[] iv = cipher.getIV();

// decrypt the file
cipher.init(Cipher.DECRYPT_MODE, secretKey, new IvParameterSpec(iv));
fis = new FileInputStream(System.getProperty("user.home") + java.io.File.separatorChar + "test.enc");
fos = new FileOutputStream(System.getProperty("user.home") + java.io.File.separatorChar + "test.pdf");
final CipherInputStream input = new CipherInputStream(fis, cipher);

final byte[] decryptedData = new byte[4096];
int decryptedRead;
while ((decryptedRead = input.read(decryptedData)) >= 0) {
    fos.write(decryptedData, 0, decryptedRead);
}
fos.flush();
fos.close();
input.close();
fis.close();
