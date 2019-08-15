public class SOEncryptDecryptExampleBytes
{
    // "thisIsASecretKey";
    private static byte[] key = { 0x74, 0x68, 0x69, 0x73, 0x49, 0x73, 0x41, 0x53, 0x65, 0x63, 0x72, 0x65, 0x74, 0x4b, 0x65, 0x79 };

    private static final String STRING_ENCODING = "UTF-8";

    public static void main(String[] args) throws Exception
    {
        //*********************************************
        String x = "Hello";
        System.out.println("Plain Text: " + x);
        String e = encryptBytesAndBase64Encode(x.getBytes(STRING_ENCODING));
        System.out.println("Encrypted: " + e);
        byte[] d = base64decodeAndDecryptBytes(e);
        System.out.println("Decrypted: " + new String(d, STRING_ENCODING));

        //*********************************************
        byte b = 124;
        System.out.println("Plain Byte: " + b);
        String eb = encryptBytesAndBase64Encode(new byte[] { b });
        System.out.println("Encrypted Byte: " + eb);
        byte[] bd = base64decodeAndDecryptBytes(eb);
        System.out.println("Decrypted Byte: " + bd[0]);

        //*********************************************
        byte[] bArray = { 23, 42, 55 };
        System.out.println("Plain Byte Array: " + Arrays.toString(bArray));
        String eba = encryptBytesAndBase64Encode(bArray);
        System.out.println("Encrypted Byte Array: " + eba);
        byte[] deba = base64decodeAndDecryptBytes(eba);
        System.out.println("Decrypted Byte Array: " + Arrays.toString(deba));
        //*********************************************
    }

    /**
     * Transforms a byte[] into an encrypted byte[] and then uses base64 encodes the encrypted byte[]
     */
    public static String encryptBytesAndBase64Encode(byte[] bytes) throws Exception
    {
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        SecretKeySpec secretKey = new SecretKeySpec(key, "AES");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        String encryptedString = Base64.encodeBase64String(cipher.doFinal(bytes));
        return encryptedString;
    }

    /**
     * Base64 decodes a string into a byte[] and then decrypts those bytes into a decrypted byte[]
     */
    public static byte[] base64decodeAndDecryptBytes(String base64EncodedEncryptedBytes) throws Exception
    {
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5PADDING");
        SecretKeySpec secretKey = new SecretKeySpec(key, "AES");
        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        byte[] decryptedBytes = cipher.doFinal(Base64.decodeBase64(base64EncodedEncryptedBytes));
        return decryptedBytes;
    }
}
