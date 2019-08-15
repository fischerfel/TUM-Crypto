public static String buildIdentity(String identity) {
    try {
        return new String(new URLCodec().encode(toSHA1(identity.getBytes("utf-8"))), "iso-8859-1");
    } catch (UnsupportedEncodingException e) {
        // should never happen, utf-8 and iso-8859-1 support is required by jvm specification. In any case, we rethrow.
        throw new RuntimeException(e);
    }
}

public static byte[] toSHA1(byte[] convertme) throws UnsupportedEncodingException {
    try {
        MessageDigest md = MessageDigest.getInstance("SHA-1");
        md.update(convertme);
        return md.digest();
    } catch (NoSuchAlgorithmException e) {
        throw new RuntimeException(e);
    }
}

public static void main(String[] args) {
    System.out.println(buildIdentity("asfasfasdf"));
}
