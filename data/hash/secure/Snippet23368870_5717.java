public static String digestHex(String text) {
    StringBuilder stringBuffer = new StringBuilder();
    try {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");// SHA-256
        digest.reset();
        for (byte b : digest.digest(text.getBytes("UTF-8"))) {
            stringBuffer.append(Integer.toHexString((int) (b & 0xff)));
        }
    } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
        e.printStackTrace();
    }
    return stringBuffer.toString();
}
