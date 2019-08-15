    void hashBigInteger(String s) {
    try {
        BigInteger a = new BigInteger(s, 16);
        MessageDigest sha = MessageDigest.getInstance("SHA-256");
        byte[] b = a.toByteArray();
        sha.update(b, 0, b.length);
        byte[] digest = sha.digest();
        BigInteger d = new BigInteger(digest);
        Log.d("HASH", "H = " + d.toString(16));
    } catch (NoSuchAlgorithmException e) {
        throw new UnsupportedOperationException(e);
    }
}
