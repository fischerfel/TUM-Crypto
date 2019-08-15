public String calculateMD5(String string) {
    StringBuilder result = new StringBuilder();
    try {
        MessageDigest m = MessageDigest.getInstance("MD5");
        m.update(string.getBytes("UTF8"));

        byte s[] = m.digest();

        for (int i = 0; i < s.length; i++) {
            result.append(Integer.toHexString((0x000000ff & s[i]) | 0xffffff00).substring(6));
        }
    } catch (NoSuchAlgorithmException e) {
        throw new IllegalStateException("Password hash is unsupported by device android implementation.", e);
    } catch (UnsupportedEncodingException e) {
        throw new IllegalStateException("Password hash is unsupported by device android implementation.", e);
    }
    return result.toString();
}
