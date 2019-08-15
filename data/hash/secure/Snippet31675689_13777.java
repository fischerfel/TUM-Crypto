public byte[] generateHASH(byte[] message) throws Exception {
    MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
    byte[] hash = messageDigest.digest(message);
    return hash;
}
