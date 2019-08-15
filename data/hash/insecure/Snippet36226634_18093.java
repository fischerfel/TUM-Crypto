public static void main(String a[]) throws NoSuchAlgorithmException {
    String passClear = "cleartext";
    MessageDigest md5 = MessageDigest.getInstance("MD5"); // you can change it to SHA1 if needed!
    md5.update(passClear.getBytes(), 0, passClear.length());
    System.out.printf("MD5: %s: %s ", passClear, new BigInteger(1, md5.digest()).toString(16));
}
