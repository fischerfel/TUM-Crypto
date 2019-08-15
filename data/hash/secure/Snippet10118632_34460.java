public static byte[] computeDigestWithStream(ByteArrayInputStream stream) throws Exception {
    final MessageDigest digest =
            MessageDigest.getInstance("GOST3411");

    // processing data
    final DigestInputStream digestStream =
            new DigestInputStream(stream, digest);
    while (digestStream.available() != 0) {
        digestStream.read();
    }
