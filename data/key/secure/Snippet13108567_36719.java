final Key k = new SecretKeySpec(keyString.getBytes(), "AES");
Cipher c = Cipher.getInstance("AES");
c.init(Cipher.DECRYPT_MODE, k);

final InputStream in = new BufferedInputStream(new FileInputStream(fileNameToDecrypt));
final CipherInputStream instream = new CipherInputStream(in, c);

if (instream.read() != 'B') {
    System.out.println("Error");
}

if (instream.read() != 'Z') {
    System.out.println("Error");
}

final CBZip2InputStream zip = new CBZip2InputStream(instream);
