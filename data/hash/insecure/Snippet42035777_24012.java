    public static long generateId(String text) {

    byte[] buffer = null;
    MessageDigest md = null;
    try {
        md = MessageDigest.getInstance("SHA1");
        md.reset();
        buffer = text.getBytes(Charsets.UTF_8);

    } catch (NoSuchAlgorithmException e) {
        e.printStackTrace();
    }
    md.update(buffer);
    byte[] digest = md.digest();
    String hexStr = "";
    for (int i = 0; i < digest.length; i++) {
        hexStr += Integer.toString((digest[i] & 0xff) + 0x100, 16).substring(1);
    }
    long hashid = 0;
    for (int i = 0; i < hexStr.length(); i++)
        hashid += Math.abs((long) Math.pow(27, 10 - i) * ('a' - (1 + hexStr.charAt(i))));

    return hashid;
}   
