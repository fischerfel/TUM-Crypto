
public class Obscure {
private byte[] k = "Now is the time for all good men to come to the aid of their country."
        .getBytes();

public Obscure(String keyString) {
    k = keyString.getBytes();
}

public boolean encryptFile(String source, String target)
        throws NoSuchAlgorithmException, NoSuchPaddingException,
        InvalidKeyException, IOException {
    Cipher encoding;
    byte[] buffer = new byte[8192];

    FileInputStream fis = new FileInputStream(source);
    FileOutputStream fos = new FileOutputStream(target);

    SecretKeySpec key = new SecretKeySpec(k, "DES");
    encoding = Cipher.getInstance("DES");
    encoding.init(Cipher.ENCRYPT_MODE, key);
    CipherOutputStream cos = new CipherOutputStream(fos, encoding);
    int numBytes;
    while ((numBytes = fis.read(buffer)) != -1) {
        cos.write(buffer, 0, numBytes);
    }
    fos.flush();
    fis.close();
    fos.close();
    cos.close();
    return true;
}
}
