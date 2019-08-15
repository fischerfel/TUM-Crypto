public static String buildIdentity(String identity) {
    try {
        return URLEncoder.encode( new String(toSHA1(identity.getBytes("ISO-8859-1")), "ISO-8859-1"), "ISO-8859-1").toLowerCase();
    } catch (UnsupportedEncodingException e) {
        e.printStackTrace();
    }
    return null;
}

public static byte[] toSHA1(byte[] convertme){
    try {
        MessageDigest md = MessageDigest.getInstance("SHA-1");
        md.update(convertme);
        return md.digest();
    }
    catch(NoSuchAlgorithmException e) {
        e.printStackTrace();
    }
    return null;
}
