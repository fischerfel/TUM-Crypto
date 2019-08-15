public static void main(String[] args) throws NoSuchAlgorithmException, UnsupportedEncodingException {
    System.out.println(crypt("password"));
}

public static String crypt(String str) throws NoSuchAlgorithmException, UnsupportedEncodingException {
    if (str == null || str.length() == 0) {
        throw new IllegalArgumentException("String to encrypt cannot be null or zero length");
    }
    MessageDigest md = MessageDigest.getInstance("MD5");
    byte[] hash = md.digest(str.getBytes("UTF-8"));
    return toHexString(hash);
}

/**
 * Converts a byte array to hex string
 */
public static String toHexString(byte[] block) {
    StringBuffer buf = new StringBuffer();
    char[] hexChars = {
        '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
        'A', 'B', 'C', 'D', 'E', 'F'};
    int len = block.length;
    int high = 0;
    int low = 0;
    for (int i = 0; i < len; i++) {
        high = ((block[i] & 0xf0) >> 4);
        low = (block[i] & 0x0f);
        buf.append(hexChars[high]);
        buf.append(hexChars[low]);
    }
    return buf.toString();
}  
