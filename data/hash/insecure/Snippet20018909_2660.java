private static MessageDigest md;
private static byte[] XorKeyIpad, XorKeyOpad;

private static final byte ipad = 0x36;
private static final byte opad = 0x5c;
private static final byte maxsize = 64;

public static void HMAC_SHA1(String msg, String keyString) throws NoSuchAlgorithmException, UnsupportedEncodingException {

    int i;
    String keyHex = toHex(keyString);
    byte[] keyByte = keyHex.getBytes();
    md = MessageDigest.getInstance("SHA-1");

    //Verify if the Key have the right size;
    if (keyByte.length > maxsize) {
        keyByte = md.digest(keyByte);
        md.reset();
    }
    System.out.println("KeyByte: " + keyByte + " Length: " + keyByte.length);

    //XOR between Key and ipad;
    XorKeyIpad = new byte[maxsize];
    for (i = 0; i < keyByte.length; i++) {
        XorKeyIpad[i] = (byte) (keyByte[i] ^ ipad);
    }
    System.out.println("XorKeyIpad: " + XorKeyIpad + "  Ipad: " + ipad);
    md.update(XorKeyIpad);
    XorKeyIpad = md.digest(XorKeyIpad);       


    //Concat the XOR between Key and ipad with the message;
    String concatXorKIM = concatByteString(XorKeyIpad, msg);


    //SHA1 of the concat the XOR between Key and ipad with the message(which i call X);
    md.update(concatXorKIM.getBytes());
    byte[] hashConcatXorKIM = md.digest();
    md.reset();


    //XOR between Key and opad(which i call Y);
    XorKeyOpad = new byte[maxsize];
    for (i = 0; i < keyByte.length; i++) {
        XorKeyOpad[i] = (byte) (keyByte[i] ^ opad);
    }

    //Concat between X and Y;
    byte[] concatHashXorKI_XorKO = concatByteByte(hashConcatXorKIM, XorKeyOpad);

    //SHA1 of the variable concatHashXorKI_XorKO
    md.update(concatHashXorKI_XorKO);
    byte[] hmac = md.digest();
    md.reset();
}


public static String toHex(String arg) throws UnsupportedEncodingException {
    return String.format("%040x", new BigInteger(1, arg.getBytes("UTF-8")));
}

public static String concatByteString(final byte[] bytes, final String str) {
    final StringBuilder sb = new StringBuilder();
    for (byte b : bytes) {
        sb.append(b);
    }
    sb.append(str);
    return sb.toString();
}

public static byte[] concatByteByte(final byte[] byteOne, final byte[] byteTwo) {
    byte[] concatBytes = new byte[byteOne.length + byteTwo.length];
    System.arraycopy(byteOne, 0, concatBytes, 0, byteOne.length);
    System.arraycopy(byteTwo, 0, concatBytes, byteOne.length, byteTwo.length);

    return concatBytes;
}

final protected static char[] hexArray = "0123456789ABCDEF".toCharArray();

public static String bytesToHex(byte[] bytes) {
    char[] hexChars = new char[bytes.length * 2];
    int v;
    for (int j = 0; j < bytes.length; j++) {
        v = bytes[j] & 0xFF;
        hexChars[j * 2] = hexArray[v >>> 4];
        hexChars[j * 2 + 1] = hexArray[v & 0x0F];
    }
    return new String(hexChars);
}`
