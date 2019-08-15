 public static void main(final String[] args) throws Exception {
    final Random random = new Random();
    final int[] bits = new int[257];
    for (int i = 0; i < 10000; i++) {
        final MessageDigest sha256 = MessageDigest.getInstance("SHA-256");
        final BigInteger testNum = new BigInteger(100, random);
        final byte[] hash = sha256.digest(testNum.toByteArray());
        final BigInteger hashedInt = new BigInteger(1, hash);
        bits[hashedInt.bitLength()]++;
    }

    for (int i = 0; i < bits.length; i++) {
        if (bits[i] > 0) {
            System.out.println(i + " / " + bits[i]);
        }
    }        
}
