    private static String SHA1(final String text) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        final MessageDigest md = MessageDigest.getInstance("SHA-1");
        md.update(text.getBytes("UTF-8"));
        return new String(org.apache.commons.codec.binary.Hex.encodeHex(md.digest()));
    }
