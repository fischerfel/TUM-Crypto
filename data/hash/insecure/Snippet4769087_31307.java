/**
 * Compute a SHA-1 hash of a String argument
 *
 * @param arg the UTF-8 String to encode
 * @return the sha1 hash as a string.
 */
public static String computeSha1OfString(String arg) {
    try {
        return computeSha1OfByteArray(arg.getBytes(("UTF-8")));
    } catch (UnsupportedEncodingException ex) {
        throw new UnsupportedOperationException(ex);
    }
}

private static String computeSha1OfByteArray(byte[] arg) {
    try {
        MessageDigest md = MessageDigest.getInstance("SHA-1");
        md.update(arg);
        byte[] res = md.digest();
        return toHexString(res);
    } catch (NoSuchAlgorithmException ex) {
        throw new UnsupportedOperationException(ex);
    }
}

private static String toHexString(byte[] v) {
    StringBuilder sb = new StringBuilder(v.length * 2);
    for (int i = 0; i < v.length; i++) {
        int b = v[i] & 0xFF;
        sb.append(HEX_DIGITS.charAt(b >>> 4)).append(HEX_DIGITS.charAt(b & 0xF));
    }
    return sb.toString();
}
