public static String computeSha1OfString(final String message) 
    throws UnsupportedOperationException, NullPointerException {
        try {
               return computeSha1OfByteArray(message.getBytes(("UTF-8")));
        } catch (UnsupportedEncodingException ex) {
                throw new UnsupportedOperationException(ex);
        }
}

private static String computeSha1OfByteArray(final byte[] message)
    throws UnsupportedOperationException {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            md.update(message);
            byte[] res = md.digest();
            return toHexString(res);
        } catch (NoSuchAlgorithmException ex) {
            throw new UnsupportedOperationException(ex);
        }
}
