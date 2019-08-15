public static void main(String[] args) {
    try {
        byte[] md5hex = MessageDigest.getInstance("MD5").digest("abc".getBytes());
        System.out.println(new BigInteger(bytesToHex(md5hex), 16));
    } catch (NoSuchAlgorithmException e) {
        e.printStackTrace();
    }
}
