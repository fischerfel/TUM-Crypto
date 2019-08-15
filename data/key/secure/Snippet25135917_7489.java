public static String encrypt(String input, String key) throws Exception {
    SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes(), "AES");
    Cipher cipher = Cipher.getInstance("AES/ECB/NoPadding");
    cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
    return DatatypeConverter.printHexBinary(cipher.doFinal(padToMultipleOf32(input.getBytes())));
}

public static byte[] padToMultipleOf32(final byte[] bytes) {
    int n16 = ((bytes.length + 31) / 32);
    int paddedLen = n16 * 32;

    byte[] result = new byte[paddedLen];

    for (int i = 0; i < bytes.length; i++) {
        result[i] = bytes[i];
    }

    for (int i = bytes.length; i < paddedLen; i++) {
        result[i] = 0x00;
    }

    System.out.println(new String(result).length());

    return result;
}
