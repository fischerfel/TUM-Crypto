public class SimpleLongHash {
    final MessageDigest md;
    //
    public SimpleLongHash() throws NoSuchAlgorithmException {
        md = MessageDigest.getInstance("MD5");
    }
    //
    public long hash(final String str) {
        return hash(str.getBytes());
    }
    public long hash(final byte[] buf) {
        md.reset();
        final byte[] digest = md.digest(buf);
        return (getLong(digest, 0) ^ getLong(digest, 8));
    }
    //
    private static final long getLong(final byte[] array, final int offset) {
        long value = 0;
        for (int i = 0; i < 8; i++) {
            value = ((value << 8) | (array[offset+i] & 0xFF));
        }
        return value;
    }
}
