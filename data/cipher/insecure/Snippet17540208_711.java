public static String toHex(byte[] buf) {
    if (buf == null)
            return "";
    StringBuffer result = new StringBuffer(2*buf.length);
    for (int i = 0; i < buf.length; i++) {
            appendHex(result, buf[i]);
    }
    return result.toString();
}

private static void appendHex(StringBuffer sb, byte b) {
    sb.append(HEX.charAt((b>>4)&0x0f)).append(HEX.charAt(b&0x0f));
}

private final static String HEX = "0123456789ABCDEF";

public static String encrypt(String seed, String cleartext) throws Exception {
    byte[] rawKey = getRawKey(seed.getBytes());
    byte[] result = encrypt(rawKey, cleartext.getBytes());
    return toHex(result);
}    

private static byte[] encrypt(byte[] raw, byte[] clear) throws Exception {
    SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
        Cipher cipher = Cipher.getInstance("AES");
    cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
    byte[] encrypted = cipher.doFinal(clear);
        return encrypted;
}
