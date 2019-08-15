try {
    return MessageDigest.getInstance("MD5");
} catch (NoSuchAlgorithmException e) {
    throw (AssertionError)new AssertionError("unreachable").initCause(e);
}
