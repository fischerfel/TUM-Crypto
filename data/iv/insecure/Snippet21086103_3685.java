private static void runEncryption() throws Exception
{
    String plainText = "The quick BROWN fox jumps over the lazy dog!";

    Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");

    SecretKeySpec keySpec = new SecretKeySpec(hexToBytes("D41D8CD98F00B2040000000000000000"), 0, 16, "AES");
    IvParameterSpec ivSpec = new IvParameterSpec(hexToBytes("03B13BBE886F00E00000000000000000"));

    cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivSpec);
    byte[] encrypted = cipher.doFinal(plainText.getBytes("UTF-8"));

    String encryptedHexDump = bytesToHex(encrypted);
    String encryptedBase64 = new String(DatatypeConverter.printBase64Binary(encrypted));

    System.out.println("Encrypted hex dump = " + encryptedHexDump);
    System.out.println("");
    System.out.println("Encrypted base64 = " + encryptedBase64);
}

private static byte[] hexToBytes(String s)
{
    int len = s.length();
    byte[] data = new byte[len / 2];

    for (int i = 0; i < len; i += 2)
        data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4) + Character.digit(s.charAt(i + 1), 16));

    return data;
}

final protected static char[] hexArray = "0123456789abcdef".toCharArray();

public static String bytesToHex(byte[] bytes)
{
    char[] hexChars = new char[bytes.length * 2];
    for (int j = 0; j < bytes.length; j++)
    {
        int v = bytes[j] & 0xFF;
        hexChars[j * 2] = hexArray[v >>> 4];
        hexChars[j * 2 + 1] = hexArray[v & 0x0F];
    }
    return new String(hexChars);
}
