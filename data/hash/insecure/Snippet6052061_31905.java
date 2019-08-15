private MessageDigest getMessageDigest() {
    try {
        return MessageDigest.getInstance("MD5");
    } catch (NoSuchAlgorithmException e) {
        throw new Error(e);
    }
}
