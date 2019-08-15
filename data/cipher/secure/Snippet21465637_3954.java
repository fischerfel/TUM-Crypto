File file = new File("myFile.txt");
String key = readFile(new File("AESKey.bin"));
String iv = readFile(new File("AESIV.bin"));
final byte[] secretKey = key.getBytes();
final byte[] initVector = iv.getBytes();
InputStream cipherInputStream = null;
final StringBuilder output = new StringBuilder();
Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding");
cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(secretKey, "AES"), new
IvParameterSpec(initVector, 0, cipher.getBlockSize()));
cipherInputStream = new CipherInputStream(new FileInputStream(file), cipher);
final byte[] buffer = new byte[8192];
int read = cipherInputStream.read(buffer);
final String charsetName = "UTF-8";
while (read > -1) {
    output.append(new String(buffer, 0, read, charsetName));
read = cipherInputStream.read(buffer);
}
System.out.println(output);*
