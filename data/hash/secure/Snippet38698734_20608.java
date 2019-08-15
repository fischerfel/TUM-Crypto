// HasherImpl class:
public String sha256(final InputStream stream) throws IOException, NoSuchAlgorithmException {
    final MessageDigest digest = MessageDigest.getInstance("SHA-256");
    final byte[] bytesBuffer = new byte[300000]; 
    int bytesRead = -1;
    while ((bytesRead = stream.read(bytesBuffer)) != -1) {
        digest.update(bytesBuffer, 0, bytesRead);
    }
    final byte[] hashedBytes = digest.digest();
    return bytesToHex(hashedBytes);
}
