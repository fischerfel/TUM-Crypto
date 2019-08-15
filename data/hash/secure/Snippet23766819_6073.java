public String sha256(String original) throws NoSuchAlgorithmException {
    MessageDigest md = MessageDigest.getInstance("SHA-256");
    md.update(original.getBytes());
    byte[] digest = md.digest();
    return new String(Hex.encodeHexString(digest));
}
