public static void main(final String ... args) throws Exception {
    final SecureRandom prng = new SecureRandom();
    final byte[] aes128KeyData  = new byte[128 / Byte.SIZE];
    prng.nextBytes(aes128KeyData);
    final SecretKey aesKey = new SecretKeySpec(aes128KeyData, "AES");
    System.out.println(toHex(aesKey.getEncoded()));
}

private static String toHex(final byte[] data) {
    final StringBuilder sb = new StringBuilder(data.length * 2);
    for (final byte b : data) {
        sb.append(String.format("%02X", b));
    }
    return sb.toString();
}
