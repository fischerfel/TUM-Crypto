 public class AESCrypt {

    static String plainText;
    static byte[] plainBytesDecrypted = new byte[1024];
    static byte[] cipherBytes = new byte[1024];
    static SecretKey key;
    static Cipher cipher;

    public AESCrypt() throws NoSuchAlgorithmException {
        KeyGenerator generator = KeyGenerator.getInstance("AES");

        generator.init(128);
        key = generator.generateKey();

    }

    public static byte[] encryption(String plainBytes) {

        try {
            Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());

            cipher = Cipher.getInstance("AES/CBC/PKCS5Padding", "BC");
            cipher.init(Cipher.ENCRYPT_MODE, key);
            cipherBytes = cipher.doFinal(plainBytes.getBytes());
            System.out.println("Encrypted data : " + new String(cipherBytes));

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return cipherBytes;
    }

    private static String bytesToHex(byte[] hash) {

        return DatatypeConverter.printHexBinary(hash);

    }

    public static String Decryption() throws InvalidKeyException, IllegalBlockSizeException, InvalidAlgorithmParameterException, BadPaddingException {

        cipher.init(Cipher.DECRYPT_MODE, key, cipher.getParameters());
        plainBytesDecrypted = cipher.doFinal(cipherBytes);

        System.out.println("Decrypted data : " + new String(plainBytesDecrypted));

        return (new String(plainBytesDecrypted));
    }
}
