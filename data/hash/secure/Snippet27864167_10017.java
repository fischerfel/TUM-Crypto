public byte[] makeDigest(String value, byte[] salt) throws NoSuchAlgorithmException {
    MessageDigest md = MessageDigest.getInstance("SHA-256");
    md.update(salt);
    return md.digest(value.getBytes());
}
