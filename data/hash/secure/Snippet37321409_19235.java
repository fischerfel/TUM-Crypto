private byte[] hash(byte[] bytes, byte[] salt) {
    MessageDigest digester = MessageDigest.getInstance("SHA-256");
    digester.update(salt);
    digester.update(bytes);
    byte[] hashed = digester.digest();
    return hashed;
}
