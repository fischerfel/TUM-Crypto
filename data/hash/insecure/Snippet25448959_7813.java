import org.apache.commons.codec.binary.Base64;

// ...

public static String hashValue(String value) {
    String digest = null;
    try {
        MessageDigest md = MessageDigest.getInstance("MD5");
        byte[] hash = md.digest(value.getBytes("UTF-16LE")); // <-- note encoding
        return new String(Base64.encodeBase64(hash));
    } catch (NoSuchAlgorithmException e) {
        e.printStackTrace();
    } catch (UnsupportedEncodingException e) {
        e.printStackTrace();
    }
    return null;
}
