/**
 * Generate MD5 hash for the given String.
 * @param string The String to generate the MD5 hash for.
 * @return The 32-char hexadecimal MD5 hash of the given String.
 */
public static String hashMD5(String string) {
    byte[] hash;

    try {
        hash = MessageDigest.getInstance("MD5").digest(string.getBytes("UTF-8"));
    } catch (NoSuchAlgorithmException e) {
        // Unexpected exception. "MD5" is just hardcoded and supported.
        throw new RuntimeException("MD5 should be supported?", e);
    } catch (UnsupportedEncodingException e) {
        // Unexpected exception. "UTF-8" is just hardcoded and supported.
        throw new RuntimeException("UTF-8 should be supported?", e);
    }

    StringBuilder hex = new StringBuilder(hash.length * 2);
    for (byte b : hash) {
        if ((b & 0xff) < 0x10) hex.append("0");
        hex.append(Integer.toHexString(b & 0xff));
    }
    return hex.toString();
}
