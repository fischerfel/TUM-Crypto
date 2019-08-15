package tutorial;

import org.apache.thrift.TByteArrayOutputStream;
import org.apache.thrift.transport.TMemoryInputTransport;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TTransportException;
import org.apache.thrift.transport.TTransportFactory;

import javax.crypto.Cipher;
import java.security.Key;
/**
 * TEncryptedFramedTransport is a buffered TTransport. It encrypts fully read message
 * with the "AES/ECB/PKCS5Padding" symmetric algorithm and send it, preceeding with a 4-byte frame size.
 */
public class TEncryptedFramedTransport extends TTransport {
    public static final String ALGORITHM = "AES/ECB/PKCS5Padding";

    private Cipher encryptingCipher;
    private Cipher decryptingCipher;

    protected static final int DEFAULT_MAX_LENGTH = 0x7FFFFFFF;

    private int maxLength_;

    private TTransport transport_ = null;

    private final TByteArrayOutputStream writeBuffer_ = new TByteArrayOutputStream(1024);
    private TMemoryInputTransport readBuffer_ = new TMemoryInputTransport(new byte[0]);

    public static class Factory extends TTransportFactory {
        private int maxLength_;
        private Key secretKey_;

        public Factory(Key secretKey) {
            this(secretKey, DEFAULT_MAX_LENGTH);
        }

        public Factory(Key secretKey, int maxLength) {
            maxLength_ = maxLength;
            secretKey_ = secretKey;
        }

        @Override
        public TTransport getTransport(TTransport base) {
            return new TEncryptedFramedTransport(base, secretKey_, maxLength_);
        }
    }

    /**
     * Constructor wraps around another tranpsort
     */
    public TEncryptedFramedTransport(TTransport transport, Key secretKey, int maxLength) {
        transport_ = transport;
        maxLength_ = maxLength;

        try {
            encryptingCipher = Cipher.getInstance(ALGORITHM);
            encryptingCipher.init(Cipher.ENCRYPT_MODE, secretKey);

            decryptingCipher = Cipher.getInstance(ALGORITHM);
            decryptingCipher.init(Cipher.DECRYPT_MODE, secretKey);
        } catch (Exception e) {
            throw new RuntimeException("Unable to initialize ciphers.");
        }
    }

    public TEncryptedFramedTransport(TTransport transport, Key secretKey) {
        this(transport, secretKey, DEFAULT_MAX_LENGTH);
    }

    public void open() throws TTransportException {
        transport_.open();
    }

    public boolean isOpen() {
        return transport_.isOpen();
    }

    public void close() {
        transport_.close();
    }

    public int read(byte[] buf, int off, int len) throws TTransportException {
        if (readBuffer_ != null) {
            int got = readBuffer_.read(buf, off, len);
            if (got > 0) {
                return got;
            }
        }

        // Read another frame of data
        readFrame();

        return readBuffer_.read(buf, off, len);
    }

    @Override
    public byte[] getBuffer() {
        return readBuffer_.getBuffer();
    }

    @Override
    public int getBufferPosition() {
        return readBuffer_.getBufferPosition();
    }

    @Override
    public int getBytesRemainingInBuffer() {
        return readBuffer_.getBytesRemainingInBuffer();
    }

    @Override
    public void consumeBuffer(int len) {
        readBuffer_.consumeBuffer(len);
    }

    private final byte[] i32buf = new byte[4];

    private void readFrame() throws TTransportException {
        transport_.readAll(i32buf, 0, 4);
        int size = decodeFrameSize(i32buf);

        if (size < 0) {
            throw new TTransportException("Read a negative frame size (" + size + ")!");
        }

        if (size > maxLength_) {
            throw new TTransportException("Frame size (" + size + ") larger than max length (" + maxLength_ + ")!");
        }

        byte[] buff = new byte[size];
        transport_.readAll(buff, 0, size);

        try {
            buff = decryptingCipher.doFinal(buff);
        } catch (Exception e) {
            throw new TTransportException(0, e);
        }

        readBuffer_.reset(buff);
    }

    public void write(byte[] buf, int off, int len) throws TTransportException {
        writeBuffer_.write(buf, off, len);
    }

    @Override
    public void flush() throws TTransportException {
        byte[] buf = writeBuffer_.get();
        int len = writeBuffer_.len();
        writeBuffer_.reset();

        try {
            buf = encryptingCipher.doFinal(buf, 0, len);
        } catch (Exception e) {
            throw new TTransportException(0, e);
        }

        encodeFrameSize(buf.length, i32buf);
        transport_.write(i32buf, 0, 4);
        transport_.write(buf);
        transport_.flush();
    }

    public static void encodeFrameSize(final int frameSize, final byte[] buf) {
        buf[0] = (byte) (0xff & (frameSize >> 24));
        buf[1] = (byte) (0xff & (frameSize >> 16));
        buf[2] = (byte) (0xff & (frameSize >> 8));
        buf[3] = (byte) (0xff & (frameSize));
    }

    public static int decodeFrameSize(final byte[] buf) {
        return
                ((buf[0] & 0xff) << 24) |
                        ((buf[1] & 0xff) << 16) |
                        ((buf[2] & 0xff) << 8) |
                        ((buf[3] & 0xff));
    }
}
