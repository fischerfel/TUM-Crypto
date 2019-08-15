// --- X509_NAME -----------------------------------------------------------
public static int X509_NAME_hash(X500Principal principal) {
    return X509_NAME_hash(principal, "SHA1");
}

private static int X509_NAME_hash(X500Principal principal, String algorithm) {
    try {
        byte[] digest = MessageDigest.getInstance(algorithm).digest(principal.getEncoded());
        return Memory.peekInt(digest, 0, ByteOrder.LITTLE_ENDIAN);
    } catch (NoSuchAlgorithmException e) {
        throw new AssertionError(e);
    }
}
