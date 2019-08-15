public class AESencrypt {

    private static final String ALGO = "AES/CBC/PKCS5Padding";
    private static final byte[] keyValue = new byte[]{'T', 'h', 'e', 'B', 'e', 's', 't', 'S', 'e', 'c', 'r', 'e', 't', 'K', 'e', 'y'};
    private static byte[] iv = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };

    public void String encryptToFile(String filename, String data) throws Exception {
        Key key = new SecretKeySpec(keyValue, "AES");
        Cipher c = Cipher.getInstance(ALGO);
        IvParameterSpec ivspec = new IvParameterSpec(iv);
        c.init(Cipher.ENCRYPT_MODE, key, ivspec);
        byte[] encVal = c.doFinal(data.getBytes());
        FileOutputStream fileOutputStream = new FileOutputStream(filename);
        fileOutputStream.write(encVal);
        fileOutputStream.close();
    }

    public static void main(String[] args) throws Exception {
        encryptToFile("foo.aes", "hellothere");
    }
}
