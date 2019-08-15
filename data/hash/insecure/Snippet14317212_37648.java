import org.apache.commons.codec.binary.Hex;

public static String hexSHA1() {
    value = "test";
    try {
        MessageDigest md;
        md = MessageDigest.getInstance("SHA-1");
        md.update(value.getBytes("utf-8"));
        byte[] digest = md.digest();

        return byteToHexString(digest);
    } catch (Exception ex) {
        return null;
    }
}


public static String byteToHexString(byte[] bytes) {
    // a94a8fe5ccb19ba61c4c0873d391e987982fbbd3
    return String.valueOf(Hex.encodeHex(bytes));
}
