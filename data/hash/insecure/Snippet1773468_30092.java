try {
    hash = MessageDigest.getInstance("MD5").digest(string.getBytes("UTF-8"));
} catch (NoSuchAlgorithmException e) {
    // Unexpected exception. "MD5" is just hardcoded and supported.
    throw new RuntimeException("MD5 should be supported?", e);
} catch (UnsupportedEncodingException e) {
    // Unexpected exception. "UTF-8" is just hardcoded and supported.
    throw new RuntimeException("UTF-8 should be supported?", e);
}
