public static String digest(String alg, String input) {
    try {
        MessageDigest md = MessageDigest.getInstance(alg);
        byte[] buffer = input.getBytes("UTF-8");
        md.update(buffer);
        byte[] digest = md.digest();
        return encodeHex(digest);
    } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
        e.printStackTrace();
        return e.getMessage();
    }
}
