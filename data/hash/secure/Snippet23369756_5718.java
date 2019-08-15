// Your original - with the horrible exception hiding removed.
public static String digestHex(String text) throws NoSuchAlgorithmException, UnsupportedEncodingException {
    StringBuilder stringBuffer = new StringBuilder();
    MessageDigest digest = MessageDigest.getInstance("SHA-256");// SHA-256
    digest.reset();
    for (byte b : digest.digest(text.getBytes("UTF-8"))) {
        stringBuffer.append(Integer.toHexString((int) (b & 0xff)));
    }
    return stringBuffer.toString();
}

// Uses BigInteger.
public static String digest(String text, int base) throws NoSuchAlgorithmException, UnsupportedEncodingException {
    MessageDigest digest = MessageDigest.getInstance("SHA-256");// SHA-256
    digest.reset();
    BigInteger b = new BigInteger(digest.digest(text.getBytes("UTF-8")));
    return b.toString(base);
}

public void test() throws NoSuchAlgorithmException, UnsupportedEncodingException {
    System.out.println("Hex:" + digestHex("1234"));
    System.out.println("Hex:" + digest("1234", 16));
    System.out.println("36:" + digest("1234", 36));
    System.out.println("Max:" + digest("1234", Character.MAX_RADIX));
}
