public final class CipherSpike2 {

  private static final byte[] SECRET_KEY = new byte[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16};

  public static void main(String[] args)
  throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, IOException {
    encryptDecrypt(511);
    encryptDecrypt(512);
  }

  private static void encryptDecrypt(int i)
  throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IOException {

    byte[] clearText = generateClearText(i);
    System.out.println("Clear text length: " + clearText.length);

    ByteArrayOutputStream bos = new ByteArrayOutputStream();
    CipherOutputStream cos = new CipherOutputStream(bos, getCipher(Cipher.ENCRYPT_MODE));
    cos.write(clearText);
    cos.close();

    final byte[] content = bos.toByteArray();
    System.out.println("written bytes: " + content.length);

    CipherInputStream
    inputStream =
    new CipherInputStream(new ByteArrayInputStream(content), getCipher(Cipher.DECRYPT_MODE));

    inputStream.read();
    inputStream.close();
 }

 private static byte[] generateClearText(int size) {
    return new byte[size];
  }

  private static Cipher getCipher(int encryptMode)
  throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException {
    Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
    SecretKeySpec key = new SecretKeySpec(SECRET_KEY, "AES");
    cipher.init(encryptMode, key);
    return cipher;
  }
}
