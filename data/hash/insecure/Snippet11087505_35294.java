// This replicates the PHP sha1 so that we can authenticate the same users.
public static String sha1(String s) throws NoSuchAlgorithmException, UnsupportedEncodingException {
    return byteArray2Hex(MessageDigest.getInstance("SHA1").digest(s.getBytes("UTF-8")));
}

private static final char[] hex = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
private static String byteArray2Hex(byte[] bytes) {
    StringBuilder sb = new StringBuilder(bytes.length * 2);
    for (final byte b : bytes) {
        sb.append(hex[(b & 0xF0) >> 4]);
        sb.append(hex[b & 0x0F]);
    }
    return sb.toString();
}
