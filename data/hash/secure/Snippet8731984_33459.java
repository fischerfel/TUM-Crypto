public String digestString (String stringToHash) throws NoSuchAlgorithmException {
    MessageDigest sha256 = MessageDigest.getInstance("SHA-256");        
    byte[] stringBytes = stringToHash.getBytes();
    byte[] stringDigest = sha256.digest(stringBytes);
    return new String(stringDigest);
}
