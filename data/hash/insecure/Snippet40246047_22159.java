private static class HashFunc {

    static MessageDigest md5;

    static {
        try {
            md5 = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            //
        }
    }

    public synchronized int hash(String s) {
        md5.update(StandardCharsets.UTF_8.encode(s));
        return new BigInteger(1, md5.digest()).intValue();
    }
}
