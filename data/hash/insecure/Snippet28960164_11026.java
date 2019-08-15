public static String calculateEtag(final String s) throws java.security.NoSuchAlgorithmException {
    final java.nio.ByteBuffer buf = java.nio.charset.StandardCharsets.UTF_8.encode(s);
    final java.security.MessageDigest digest = java.security.MessageDigest.getInstance("SHA1");
    buf.mark();
    digest.update(buf);
    buf.reset();
    return String.format("W/\"%s\"", javax.xml.bind.DatatypeConverter.printHexBinary(digest.digest()));
}
