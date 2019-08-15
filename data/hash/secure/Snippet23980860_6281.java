/**
 * Immutable class that represents a unique key for a string. This unique key
 * can be used as a key in a hash map, without the likelihood of a collision:
 * generating the same key for a different String. Note that you should first
 * check if keeping a key to a reference of a string is feasible, in that case a
 * {@link Set} may suffice.
 * <P>
 * This class utilizes SHA-512 to generate the keys and uses
 * {@link StandardCharsets#UTF_8} for the encoding of the strings. If a smaller
 * output size than 512 bits (64 bytes) is required then the leftmost bytes of
 * the SHA-512 hash are used. Smaller keys are therefore contained in with
 * larger keys over the same String value.
 * <P>
 * Note that it is not impossible to create collisions for key sizes up to 8-20
 * bytes.
 * 
 * @author owlstead
 */
public final class UniqueKey implements Serializable {

    public static final int MIN_DIGEST_SIZE_BYTES = 8;
    public static final int MAX_DIGEST_SIZE_BYTES = 64;

    /**
     * Creates a unique key for a string with the maximum size of 64 bytes.
     * 
     * @param input
     *            the input, not null
     * @return the generated instance
     */
    public static UniqueKey createUniqueKey(final CharSequence input) {
        return doCreateUniqueKey(input, MAX_DIGEST_SIZE_BYTES);
    }

    /**
     * Creates a unique key for a string with a size of 8 to 64 bytes.
     * 
     * @param input
     *            the input, not null
     * @param outputSizeBytes
     *            the output size
     * @return the generated instance
     */
    public static UniqueKey createUniqueKey(final CharSequence input,
            final int outputSizeBytes) {
        return doCreateUniqueKey(input, outputSizeBytes);
    }

    @Override
    public boolean equals(final Object obj) {
        if (!(obj instanceof UniqueKey)) {
            return false;
        }
        final UniqueKey that = (UniqueKey) obj;
        return ByteBuffer.wrap(this.key).equals(ByteBuffer.wrap(that.key));
    }

    @Override
    public int hashCode() {
        return ByteBuffer.wrap(this.key).hashCode();
    }

    /**
     * Outputs an - in itself - unique String representation of this key.
     * 
     * @return the string <CODE>"{key: [HEX ENCODED KEY]}"</CODE>
     */
    @Override
    public String toString() {
        // non-optimal but readable conversion to hexadecimal
        final StringBuilder sb = new StringBuilder(this.key.length * 2);
        sb.append("{Key: ");
        for (int i = 0; i < this.key.length; i++) {
            sb.append(String.format("%02X", this.key[i]));
        }
        sb.append("}");
        return sb.toString();
    }

    /**
     * Makes it possible to retrieve the underlying key data (e.g. to use a
     * different encoding).
     * 
     * @return the data in a read only ByteBuffer
     */
    public ByteBuffer asReadOnlyByteBuffer() {
        return ByteBuffer.wrap(this.key).asReadOnlyBuffer();
    }

    private static final long serialVersionUID = 1L;

    private static final int BUFFER_SIZE = 512;

    // byte array instead of ByteBuffer to support serialization
    private final byte[] key;

    private static UniqueKey doCreateUniqueKey(final CharSequence input,
            final int outputSizeBytes) {

        // --- setup digest

        final MessageDigest digestAlgorithm;
        try {
            // note: relatively fast on 64 bit systems (faster than SHA-256!)
            digestAlgorithm = MessageDigest.getInstance("SHA-512");
        } catch (final NoSuchAlgorithmException e) {
            throw new IllegalStateException(
                    "SHA-256 should always be avialable in a Java RE");
        }

        // --- validate input parameters

        if (outputSizeBytes < MIN_DIGEST_SIZE_BYTES
                || outputSizeBytes > MAX_DIGEST_SIZE_BYTES) {
            throw new IllegalArgumentException(
                    "Unique key size either too small or too big");
        }

        // --- setup loop

        final CharsetEncoder encoder = StandardCharsets.UTF_8.newEncoder();
        final CharBuffer buffer = CharBuffer.wrap(input);
        final ByteBuffer encodedBuffer = ByteBuffer.allocate(BUFFER_SIZE);
        CoderResult coderResult;

        // --- loop over all characters
        // (instead of encoding everything to byte[] at once - peak memory!)

        while (buffer.hasRemaining()) {
            coderResult = encoder.encode(buffer, encodedBuffer, false);
            if (coderResult.isError()) {
                throw new IllegalArgumentException(
                        "Invalid code point in input string");
            }
            encodedBuffer.flip();
            digestAlgorithm.update(encodedBuffer);
            encodedBuffer.clear();
        }

        coderResult = encoder.encode(buffer, encodedBuffer, true);
        if (coderResult.isError()) {
            throw new IllegalArgumentException(
                    "Invalid code point in input string");
        }
        encodedBuffer.flip();
        digestAlgorithm.update(encodedBuffer);
        // no need to clear encodedBuffer if generated locally

        // --- resize result if required

        final byte[] digest = digestAlgorithm.digest();
        final byte[] result;
        if (outputSizeBytes == digest.length) {
            result = digest;
        } else {
            result = Arrays.copyOf(digest, outputSizeBytes);
        }

        // --- and return the final, possibly resized, result

        return new UniqueKey(result);
    }

    private UniqueKey(final byte[] key) {
        this.key = key;
    }
}
