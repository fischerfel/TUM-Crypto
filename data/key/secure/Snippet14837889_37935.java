package com.drewnoakes.crypto;

import com.drewnoakes.util;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;

/**
 * An implementation of {@link InputStream} that builds a MAC by observing bytes as they pass through another stream.
 * <p/>
 * This implementation does not support mark/reset or seek, as all bytes of the request must be processed in order.
 * 
 * @author Drew Noakes http://drewnoakes.com
 */
public class HMACValidationStream extends InputStream
{
    private static final String HASH_ALGORITHM = "HmacSHA256";

    private final InputStream _inputStream;
    private final Mac _mac;

    public HMACValidationStream(@NotNull InputStream inputStream, @NotNull byte[] hmacSecret) throws NoSuchAlgorithmException, InvalidKeyException
    {
        _inputStream = inputStream;

        Key secretKeySpec = new SecretKeySpec(hmacSecret, HASH_ALGORITHM);

        _mac = Mac.getInstance(secretKeySpec.getAlgorithm());
        _mac.init(secretKeySpec);
    }

    /**
     * Calculates whether the built-up HMAC matches the provided one (commonly from an HTTP request header.)
     */
    public boolean matches(@NotNull String providedHMAC)
    {
        byte[] hmac = _mac.doFinal();

        String expectedHMAC = Convert.bytesToHex(hmac);
        return expectedHMAC.equals(providedHMAC);
    }

    //////////////////////////////////////////

    @Override
    public int read() throws IOException
    {
        int i = _inputStream.read();
        if (i != -1)
            _mac.update((byte)i);
        return i;
    }

    @Override
    public int read(byte[] b) throws IOException
    {
        int i = _inputStream.read(b);
        if (i != -1)
            _mac.update(b, 0, i);
        return i;
    }

    @Override
    public int read(byte[] b, int off, int len) throws IOException
    {
        int i = _inputStream.read(b, off, len);
        if (i != -1)
            _mac.update(b, off, i);
        return i;
    }

    @Override
    public long skip(long n) throws IOException
    {
        throw new IOException("Not supported");
    }

    @Override
    public int available() throws IOException
    {
        return _inputStream.available();
    }

    @Override
    public void close() throws IOException
    {
        _inputStream.close();
    }

    @Override
    public void mark(int readlimit)
    {}

    @Override
    public void reset() throws IOException
    {
        throw new IOException("Not supported");
    }

    @Override
    public boolean markSupported()
    {
        return false;
    }
}
