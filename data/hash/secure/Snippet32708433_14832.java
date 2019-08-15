public static void main(String[] args) throws Exception {
    long a = Long.MAX_VALUE;
    a >>= 0x1a;
    a ^= 0xcafebabeL;

    byte[] result = MessageDigest.getInstance("SHA-256").digest(("" + a).getBytes());
    for (byte b : result) {
        System.out.printf("%02x", b);
    }
}
