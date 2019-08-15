public static void main(String[] args) throws NoSuchAlgorithmException, UnsupportedEncodingException {
    String hexString = "90aa";
    byte[] bkey = hexToString(hexString);
    String someString = "qwe";
    MessageDigest messageDigest = MessageDigest.getInstance("MD5");
    byte[] input = join(stringToBytes(someString), bkey);
    String checksum = new BigInteger(1, messageDigest.digest(input)).toString(16);
    System.out.println(checksum);
    System.out.println(Charset.defaultCharset().displayName());
}

private static byte[] join(byte[] a, byte[] b) {
    // join two byte arrays
    final byte[] ret = new byte[a.length + b.length];
    System.arraycopy(a, 0, ret, 0, a.length);
    System.arraycopy(b, 0, ret, a.length, b.length);
    return ret;
}

public static byte[] hexToString(String hex) {
    // hexToString that works at a byte level, not a character level
    byte[] output = new byte[(hex.length() + 1) / 2];
    for (int i = hex.length() - 1; i >= 0; i -= 2) {
        int from = i - 1;
        if (from < 0) {
            from = 0;
        }
        String str = hex.substring(from, i + 1);
        output[i/2] = (byte)Integer.parseInt(str, 16);
    }
    return output;
}
public static byte[] stringToBytes(final String input) {
    // unlike Stirng.toByteArray(), we ignore any high-byte values of the characters.
    byte[] ret = new byte[input.length()];
    for (int i = input.length() - 1; i >=0; i--) {
        ret[i] = (byte)input.charAt(i);
    }
    return ret;
}
