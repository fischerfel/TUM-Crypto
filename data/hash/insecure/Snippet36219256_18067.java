private static final char[] HEX_ARRAY = "0123456789ABCDEF".toCharArray();

public static String md5string(String s) {
    return toHex(md5plain(s));
}

public static byte[] md5plain(String s) {
    final String MD5 = "MD5";
    try {
        // Create MD5 Hash
        MessageDigest digest = java.security.MessageDigest.getInstance(MD5);
        digest.update(s.getBytes());
        return digest.digest();
    } catch (NoSuchAlgorithmException e) {
        // never happens
        e.printStackTrace();
        return null;
    }
}

public static String toHex(byte[] buf) {
    char[] hexChars = new char[buf.length * 2];
    int v;
    for (int i = 0; i < buf.length; i++) {
        v = buf[i] & 0xFF;
        hexChars[i * 2] = HEX_ARRAY[v >>> 4];
        hexChars[i * 2 + 1] = HEX_ARRAY[v & 0x0F];
    }
    return new String(hexChars);
}
