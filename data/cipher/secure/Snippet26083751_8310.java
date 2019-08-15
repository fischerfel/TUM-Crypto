import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.security.*;


public class FlushableCipherOutputStream extends OutputStream
{
    private static int HEADER_LENGTH = 16;


    private SecretKeySpec key;
    private RandomAccessFile seekableFile;
    private boolean flushGoesStraightToDisk;
    private Cipher cipher;
    private boolean needToRestoreCipherState;

    /** the buffer holding one byte of incoming data */
    private byte[] ibuffer = new byte[1];

    /** the buffer holding data ready to be written out */
    private byte[] obuffer;



    /** Each time you call 'flush()', the data will be written to the operating system level, immediately available
     * for other processes to read. However this is not the same as writing to disk, which might save you some
     * data if there's a sudden loss of power to the computer. To protect against that, set 'flushGoesStraightToDisk=true'.
     * Most people set that to 'false'. */
    public FlushableCipherOutputStream(String fnm, SecretKeySpec _key, boolean append, boolean _flushGoesStraightToDisk)
            throws IOException
    {
        this(new File(fnm), _key, append,_flushGoesStraightToDisk);
    }

    public FlushableCipherOutputStream(File file, SecretKeySpec _key, boolean append, boolean _flushGoesStraightToDisk)
            throws IOException
    {
        super();

        if (! append)
            file.delete();
        seekableFile = new RandomAccessFile(file,"rw");
        flushGoesStraightToDisk = _flushGoesStraightToDisk;
        key = _key;

        try {
            cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");

            byte[] iv = new byte[16];
            byte[] headerBytes = new byte[HEADER_LENGTH];
            long fileLen = seekableFile.length();
            if (fileLen % 16L != 0L) {
                throw new IllegalArgumentException("Invalid file length (not a multiple of block size)");
            } else if (fileLen == 0L) {
                // new file

                // You can write a 16 byte file header here, including some file format number to represent the
                // encryption format, in case you need to change the key or algorithm. E.g. "100" = v1.0.0
                headerBytes[0] = 100;
                seekableFile.write(headerBytes);

                // Now appending the first IV
                SecureRandom sr = new SecureRandom();
                sr.nextBytes(iv);
                seekableFile.write(iv);
                cipher.init(Cipher.ENCRYPT_MODE, key, new IvParameterSpec(iv));
            } else if (fileLen <= 16 + HEADER_LENGTH) {
                throw new IllegalArgumentException("Invalid file length (need 2 blocks for iv and data)");
            } else {
                // file length is at least 2 blocks
                needToRestoreCipherState = true;
            }
        } catch (InvalidKeyException e) {
            throw new IOException(e.getMessage());
        } catch (NoSuchAlgorithmException e) {
            throw new IOException(e.getMessage());
        } catch (NoSuchPaddingException e) {
            throw new IOException(e.getMessage());
        } catch (InvalidAlgorithmParameterException e) {
            throw new IOException(e.getMessage());
        }
    }


    /**
     * Writes one _byte_ to this output stream.
     */
    public void write(int b) throws IOException {
        if (needToRestoreCipherState)
            restoreStateOfCipher();
        ibuffer[0] = (byte) b;
        obuffer = cipher.update(ibuffer, 0, 1);
        if (obuffer != null) {
            seekableFile.write(obuffer);
            obuffer = null;
        }
    }

    /** Writes a byte array to this output stream. */
    public void write(byte data[]) throws IOException {
        write(data, 0, data.length);
    }

    /**
     * Writes <code>len</code> bytes from the specified byte array
     * starting at offset <code>off</code> to this output stream.
     *
     * @param      data     the data.
     * @param      off   the start offset in the data.
     * @param      len   the number of bytes to write.
     */
    public void write(byte data[], int off, int len) throws IOException
    {
        if (needToRestoreCipherState)
            restoreStateOfCipher();
        obuffer = cipher.update(data, off, len);
        if (obuffer != null) {
            seekableFile.write(obuffer);
            obuffer = null;
        }
    }


    /** The tricky stuff happens here. We finalise the cipher, write it out, but then rewind the
     * stream so that we can add more bytes without padding. */
    public void flush() throws IOException
    {
        try {
            if (needToRestoreCipherState)
                return; // It must have already been flushed.
            byte[] obuffer = cipher.doFinal();
            if (obuffer != null) {
                seekableFile.write(obuffer);
                if (flushGoesStraightToDisk)
                    seekableFile.getFD().sync();
                needToRestoreCipherState = true;
            }
        } catch (IllegalBlockSizeException e) {
            throw new IOException("Illegal block");
        } catch (BadPaddingException e) {
            throw new IOException("Bad padding");
        }
    }

    private void restoreStateOfCipher() throws IOException
    {
        try {
            // I wish there was a more direct way to snapshot a Cipher object, but it seems there's not.
            needToRestoreCipherState = false;
            byte[] iv = cipher.getIV(); // To help avoid garbage, re-use the old one if present.
            if (iv == null)
                iv = new byte[16];
            seekableFile.seek(seekableFile.length() - 32);
            seekableFile.read(iv);
            byte[] lastBlockEnc = new byte[16];
            seekableFile.read(lastBlockEnc);
            cipher.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(iv));
            byte[] lastBlock = cipher.doFinal(lastBlockEnc);
            seekableFile.seek(seekableFile.length() - 16);
            cipher.init(Cipher.ENCRYPT_MODE, key, new IvParameterSpec(iv));
            byte[] out = cipher.update(lastBlock);
            assert out == null || out.length == 0;
        } catch (Exception e) {
            throw new IOException("Unable to restore cipher state");
        }
    }

    public void close() throws IOException
    {
        flush();
        seekableFile.close();
    }
}
