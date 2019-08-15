// --- X509_NAME -----------------------------------------------------------

public static int X509_NAME_hash(X500Principal principal) {
    return X509_NAME_hash(principal, "SHA1");
}

private static int X509_NAME_hash(X500Principal principal, String algorithm) {
    try {

        byte[] princ = principal.getEncoded();
        final ASN1Sequence obj = (ASN1Sequence) ASN1Object.fromByteArray( princ );

        // Remove the leading sequence ...
        final DERSet enc = (DERSet) obj.getObjectAt(0);
        final byte[] toHash = enc.getDEREncoded();

        MessageDigest md = MessageDigest.getInstance(algorithm);
        byte[] digest = md.digest(toHash);
        return Memory.peekInt(digest, 0, ByteOrder.LITTLE_ENDIAN);

    } catch (NoSuchAlgorithmException e) {
        throw new AssertionError(e);
    } catch (IOException e) {
        throw new AssertionError(e);
    }
}
