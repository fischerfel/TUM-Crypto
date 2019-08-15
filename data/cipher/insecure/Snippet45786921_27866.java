private static final char[] BASE58_CHARS = "123456789ABCDEFGHJKLMNPQRSTUVWXYZabcdefghijkmnopqrstuvwxyz".toCharArray();
private static final int LENGTH = BASE58_CHARS.length;
private static final BigInteger LENGTH_BI = BigInteger.valueOf(LENGTH);

// TODO: CHANGE THE KEY TO SOMETHING RANDOM!
private static final byte[] KEY = new byte {1,2,3,4,5,6,7,8,1,2,3,4,5,6,7,8,1,2,3,4,5,6,7,8};

public static String genSlug(long priimaryKeyId) {
    ByteBuffer bb = ByteBuffer.allocate(8);
    bb.putLong(priimaryKeyId);

    Cipher cipher = Cipher.getInstance("DESede/ECB/NoPadding");
    cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(KEY, "DESede"));
    byte[] encrypted = cipher.doFinal(bb.array());
    BigInteger bi = new BigInteger(1, encrypted);

    char[] buffer = new char[20];
    int index = 0;
    do {
        BigInteger i = bi.mod(LENGTH_BI);
        buffer[index++] = BASE58_CHARS[i.intValue()];
        bi = bi.divide(LENGTH_BI);
    } while (bi.compareTo(BigInteger.ZERO) == 1);
    return new String(buffer, 0, index);
}
