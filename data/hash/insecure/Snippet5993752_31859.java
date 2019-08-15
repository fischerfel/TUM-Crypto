public static String calculateChecksum(Serializable obj) {
    if (obj == null) {
        throw new IllegalArgumentException("The object cannot be null");
    }
    MessageDigest digest = null;
    try {
        digest = MessageDigest.getInstance("MD5");
    } catch (java.security.NoSuchAlgorithmException nsae) {
        throw new IllegalStateException("Algorithm MD5 is not present", nsae);
    }
    ByteArrayOutputStream bos = new ByteArrayOutputStream();
    ObjectOutput out = null;
    byte[] objBytes = null;
    try {
        out = new ObjectOutputStream(bos);
        out.writeObject(obj);
        objBytes = bos.toByteArray();
        out.close();
    } catch (IOException e) {
        throw new IllegalStateException(
                "There was a problem trying to get the byte stream of this object: " + obj.toString());
    }
    digest.update(objBytes);
    byte[] hash = digest.digest();
    StringBuilder hexString = new StringBuilder();
    for (int i = 0; i < hash.length; i++) {
        String hex = Integer.toHexString(0xFF & hash[i]);
        if (hex.length() == 1) {
            hexString.append('0');
        }
        hexString.append(hex);
    }
    return hexString.toString();
}
