private String hashWithMD5(String text) throws UnsupportedEncodingException, NoSuchAlgorithmException {
    MessageDigest messageDigest = MessageDigest.getInstance("MD5");
    byte[] digest = messageDigest.digest(text.getBytes("UTF-8"));

    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < digest.length; i++) {
        sb.append(Integer.toString((digest[i] & 0xff) + 0x100, 16).substring(1));
    }

    return sb.toString();
}
