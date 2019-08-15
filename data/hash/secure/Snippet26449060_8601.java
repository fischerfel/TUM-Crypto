private static final char[] HEX_ARRAY = ("0123456789ABCDEF").toCharArray();

public static String getSHA512HahsOfString(String toHash) throws NoSuchAlgorithmException, UnsupportedEncodingException {
    String hash = null;

    MessageDigest digest = MessageDigest.getInstance("SHA-512");
    byte[] bytes = toHash.getBytes("UTF-8");

    digest.update(bytes, 0, bytes.length);
    bytes = digest.digest();
    hash = bytesToHex(bytes);

    return hash;
}

public static String bytesToHex(byte[] bytes) {
    char[] hexChars = new char[bytes.length * 2];

    for (int j = 0; j < bytes.length; j++) {
        int v = bytes[j] & 0xFF;
        hexChars[j * 2] = HEX_ARRAY[v >>> 4];
        hexChars[j * 2 + 1] = HEX_ARRAY[v & 0x0F];
    }

    return new String(hexChars);
}
