public static String hash(String password, String salt) throws Exception {

    MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
    String text = password + "{" + salt + "}";
    messageDigest.update(text.getBytes("UTF-8"));
    byte[] digest = messageDigest.digest();
    StringBuilder stringBuilder = new StringBuilder(digest.length * 2);
    for(byte b: digest)
        stringBuilder.append(String.format("%02x", b & 0xff));
    return stringBuilder.toString();
}
