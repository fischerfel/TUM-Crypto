public static String SHA1(String text) {
    Log.d("SHA1:::", ":" + text);
    try {

        MessageDigest md;
        md = MessageDigest.getInstance("SHA-1");
        md.update(text.getBytes("iso-8859-1"),
                0, text.length());
        byte[] sha1hash = md.digest();
        System.out.println(("SHA1:::::" + sha1hash));
        System.out.println(toHex(sha1hash));
        return toHex(sha1hash);

    } catch (NoSuchAlgorithmException e) {
        e.printStackTrace();
    } catch (UnsupportedEncodingException e) {
        e.printStackTrace();
    }

    return null;
}

private final static String HEX = "0123456789ABCDEF";

public static String toHex(byte[] buf) {

    if (buf == null) return "";

    int l = buf.length;
    StringBuffer result = new StringBuffer(2 * l);

    for (int i = 0; i < buf.length; i++) {
        appendHex(result, buf[i]);
    }

    return result.toString();

}
