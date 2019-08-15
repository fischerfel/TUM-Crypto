public class EncryptionUtils {

private static final String AES_CIPHER_METHOD = "AES";
private static final int AES_KEY_SIZE = 128;

public static byte[] generateAesKey() throws NoSuchAlgorithmException {
    KeyGenerator keyGenerator = KeyGenerator.getInstance(AES_CIPHER_METHOD);
    keyGenerator.init(AES_KEY_SIZE);
    SecretKey key = keyGenerator.generateKey();
    return key.getEncoded();
}

public static SecretKeySpec createAesKeySpec(byte[] aesKey) {
    return new SecretKeySpec(aesKey, AES_CIPHER_METHOD);
}

public static void aesEncryptFile(File in, File out, SecretKeySpec aesKeySpec) throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, IOException {
    Cipher aesCipher = Cipher.getInstance(AES_CIPHER_METHOD);
    aesCipher.init(Cipher.ENCRYPT_MODE, aesKeySpec);
    InputStream inputStream = new FileInputStream(in);
    try {
        OutputStream outputStream = new CipherOutputStream(new FileOutputStream(out), aesCipher);
        try {
            IOUtils.copy(inputStream , outputStream);
        } finally {
            outputStream.close();
        }
    } finally {
        inputStream.close();
    }
}
}


//testcode
@Test
public void testAesEncryptFile() throws IOException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, NoSuchAlgorithmException, NoSuchPaddingException {
    byte[] aesKey = EncryptionUtils.generateAesKey();
    SecretKeySpec aesKeySpec = EncryptionUtils.createAesKeySpec(aesKey);
    EncryptionUtils.aesEncryptFile(new File("C:\\test\\test.txt"), new File("C:\\test\\test-encrypted.txt"), aesKeySpec);

    FileOutputStream outputStream = new FileOutputStream("C:\\test\\aes.key");
    outputStream.write(aesKey);
    outputStream.close();
}

@Test
public void testAesDecryptFile() throws IOException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, NoSuchAlgorithmException, NoSuchPaddingException {
    FileInputStream keyFis = new FileInputStream("C:\\test\\aes.key");
    ByteArrayOutputStream keyBaos = new ByteArrayOutputStream();
    IOUtils.copy(keyFis, keyBaos);

    SecretKeySpec keySpec = new SecretKeySpec(keyBaos.toByteArray(), "AES");
    Cipher cipher = Cipher.getInstance("AES");
    cipher.init(Cipher.DECRYPT_MODE, keySpec);

    FileInputStream fileInputStream = new FileInputStream("C:\\test\\test-encrypted.txt");
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    IOUtils.copy(fileInputStream, baos);

    byte[] decrypted = cipher.doFinal(baos.toByteArray());
    FileOutputStream outputStream = new FileOutputStream("C:\\test\\test-decrypted.txt");
    outputStream.write(decrypted);
    outputStream.close();

}
