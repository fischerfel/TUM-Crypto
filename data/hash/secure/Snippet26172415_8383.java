public static byte[] buildHashFromSeparatedCanonicalValues(final BigInteger ... numbers) {
    MessageDigest md;
    try {
        md = MessageDigest.getInstance("SHA-256");
    } catch (NoSuchAlgorithmException e) {
        throw new IllegalStateException("SHA-256 should always be available", e);
    }

    final ByteBuffer lengthBuffer = ByteBuffer.allocate(Integer.SIZE / Byte.SIZE);
    for (BigInteger number : numbers) {

        if (number == null) {
            throw new IllegalArgumentException(
                "Input objects cannot be null");
        }

        final byte[] encodedNumber = number.toByteArray();
        lengthBuffer.putInt(encodedNumber.length);
        lengthBuffer.flip();
        md.update(lengthBuffer);
        lengthBuffer.clear();
        md.update(encodedNumber);
    }

    return md.digest();
}
