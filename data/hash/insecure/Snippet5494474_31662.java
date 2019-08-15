public static String md5(String string) {
    byte[] hash;

    try {
        hash = MessageDigest.getInstance("MD5").digest(string.getBytes("UTF-8"));
    } catch (NoSuchAlgorithmException e) {
        throw new RuntimeException("Huh, MD5 should be supported?", e);
    } catch (UnsupportedEncodingException e) {
        throw new RuntimeException("Huh, UTF-8 should be supported?", e);
    }

    StringBuilder hex = new StringBuilder(hash.length * 2);

    for (byte b : hash) {
        int i = (b & 0xFF);
        if (i < 0x10) hex.append('0');
        hex.append(Integer.toHexString(i));
    }

    return hex.toString();
}
