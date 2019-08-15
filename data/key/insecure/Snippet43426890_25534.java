package nl.owlstead.stackoverflow;

import java.nio.ByteBuffer;
import java.util.Random;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.ShortBufferException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * A well-defined pseudo-random generator that is based on a stream cipher.
 * <p>
 * This class mimics the {@link Random} class method signatures; it however does currently not provide:
 * <ul>
 * <li>operations returning floats or doubles including returning a Gaussian value in the range [0, 1.0) </li>
 * <li>streams of integers or longs</li>
 * </ul>
 * due to laziness of the developer.
 * It does not allow for re-seeding as re-seeding is not defined for a stream cipher;
 * the same goes from retrieving a seed from the underlying entropy source as it hasn't got one.
 * <p>
 * It is assumed that most significant (leftmost) bytes are taken from the stream cipher first.
 * All the algorithms used to return the random values are well defined, so that compatible implementations can be generated.
 * <p>
 * Instances of this class are stateful and not thread safe.
 * 
 * @author Maarten Bodewes
 */
public class StreamCipherPseudoRandom {

    private static final long TWO_POW_48 = 1L << 48;

    private final Cipher streamCipher;

    // must be a buffer of at least 6 bytes
    // a buffer that is x times 16 is probably most efficient for AES/CTR mode encryption within getBytes(byte[])
    private final ByteBuffer zeros = ByteBuffer.allocate(64);

    /**
     * Creates a SecureRandom from a stream cipher.
     * 
     * @param streamCipher an initialized stream cipher
     * @throws NullPointerException if the cipher is <code>null</code>
     * @throws IllegalStateException if the cipher is not initialized
     * @throws IllegalArgumentException if the cipher is not a stream cipher
     */
    public StreamCipherPseudoRandom(final Cipher streamCipher) {
        if (streamCipher.getOutputSize(1) != 1) {
            throw new IllegalArgumentException("Not a stream cipher");
        }
        this.streamCipher = streamCipher;
    }

    /**
     * Generates a pseudo-random number of bytes by taking exactly the required number of bytes from the stream cipher.
     * 
     * @param data the buffer to be randomized
     */
    public void nextBytes(final byte[] data) {
        generateRandomInBuffer(ByteBuffer.wrap(data));
    }

    /**
     * Generates a pseudo-random boolean value by taking exactly 1 byte from the stream cipher,
     * returning true if and only if the returned value is odd (i.e. if the least significant bit is set to 1), false otherwise.
     * 
     * @return the random boolean
     */
    public boolean nextBoolean() {
        return (generateRandomInBuffer(ByteBuffer.allocate(Byte.BYTES)).get() & 1) == 1;
    }

    /**
     * Generates a pseudo-random <code>int</code> value by taking exactly 4 bytes from the stream cipher.
     * 
     * @return the random <code>int</code> value
     */
    public int nextInt() {
        return generateRandomInBuffer(ByteBuffer.allocate(Integer.BYTES)).getInt();
    }

    /**
     * Generates a pseudo-random <code>long</code> value by taking exactly 8 bytes from the stream cipher.
     * 
     * @return the random <code>long</code> value
     */
    public long nextLong() {
        return generateRandomInBuffer(ByteBuffer.allocate(Long.BYTES)).getLong();
    }

    /**
     * Generates a pseudo-random <code>int</code> value with <code>bits</code> random bits in the lower part of the returned integer.
     * This method takes the minimum number of bytes required to hold the required number of bits from the stream cipher (e.g. 13 bits requires 2 bytes to hold them).
     * 
     * @param bits the number of bits in the integer, between 0 and 32 
     * @return the random <code>int</code> value in the range [0, 2^n) where n is the number of bits
     */
    public int next(final int bits) {
        final int bytes = (bits + Byte.SIZE - 1) / Byte.SIZE;
        final ByteBuffer buf = ByteBuffer.allocate(Integer.BYTES);
        buf.position(Integer.BYTES - bytes);
        generateRandomInBuffer(buf);
        final long l = buf.getInt(0);
        final long m = (1L << bits) - 1;
        return (int) (l & m);
    }

    /**
     * Generates a pseudo-random <code>int</code> value in a range [0, n) by:
     * 
     * <ol>
     * <li>taking 6 bytes from the stream cipher and converting it into a number y</li>
     * <li>restart the procedure if y is larger than x * n where x is the largest value such that x * n <= 2^48
     * <li>return y % n
     * </ol>
     * 
     * An exception to this rule is for n is 1 in which case this method direct returns 0, without taking any bytes from the stream cipher.

     * @param n the maximum value (exclusive) - n must be a non-zero positive number
     * @return the random <code>int</code> value in the range [0, n)
     * @throws IllegalArgumentException if n is zero or negative 
     */
    public int nextInt(final int n) {
        if (n <= 0) {
            throw new IllegalArgumentException("max cannot be negative");
        } else if (n == 1) {
            // only one choice
            return 0;
        }

        final ByteBuffer buf = ByteBuffer.allocate(48 / Byte.SIZE);
        long maxC = TWO_POW_48 - TWO_POW_48 % n;

        long l;
        do {
            buf.clear();
            generateRandomInBuffer(buf);
            // put 16 bits into position 32 to 47
            l = (buf.getShort() & 0xFFFFL) << Integer.SIZE;
            // put 32 bits into position 0 to 31
            l |= buf.getInt() & 0xFFFFFFFFL;
        } while (l > maxC);

       return (int) (l % n);
    }

    /**
     * Retrieves random bytes from the underlying stream cipher.
     * All methods that affect the stream cipher should use this method.
     * The bytes between the position and the limit will contain the random bytes; position and limit are left unchanged.
     * <p>
     * The buffer may not be read only and must support setting a mark; previous marks are discarded.
     * 
     * @param buf the buffer to receive the bytes between the position and limit 
     * @return the same buffer, to allow for 
     */
    protected ByteBuffer generateRandomInBuffer(final ByteBuffer buf) {
        while (buf.hasRemaining()) {
            // clear the zeros buffer
            zeros.clear();
            // set the number of zeros to process
            zeros.limit(Math.min(buf.remaining(), zeros.capacity()));
            try {
                // process the zero's into buf (note that the input size is leading)
                buf.mark();
                streamCipher.update(zeros, buf);
            } catch (ShortBufferException e) {
                // not enough output size, which cannot be true for a stream cipher
                throw new IllegalStateException(
                        String.format("Cipher %s not behaving as a stream cipher", streamCipher.getAlgorithm()));
            }
        }
        buf.reset();
        return buf;
    }

    public static void main(String[] args) throws Exception {
        Cipher streamCipher = Cipher.getInstance("AES/CTR/NoPadding");
        // zero key and iv for demo purposes only
        SecretKey aesKey = new SecretKeySpec(new byte[24], "AES");
        IvParameterSpec iv = new IvParameterSpec(new byte[16]);
        streamCipher.init(Cipher.ENCRYPT_MODE, aesKey, iv);

        StreamCipherPseudoRandom rng = new StreamCipherPseudoRandom(streamCipher);
        // chosen by fair dice roll, guaranteed to be random
        System.out.println(rng.nextInt(6) + 1);
    }
}
