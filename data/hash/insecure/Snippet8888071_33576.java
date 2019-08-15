/**
 * returns the first 64 bits of the md5 digest as a long
 */
public static long get64BitMD5(String text) {
    try {
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(text.getBytes(UTF_8));
        byte [] digest = md.digest();
        long ret = 0;
        for (int i = 0; i < 8; i++)
            ret = (ret<<8)|(((long)digest[i])&0xFFl);
        return ret;
    } catch (NoSuchAlgorithmException e) {
        throw new RuntimeException(e);
    }
}
