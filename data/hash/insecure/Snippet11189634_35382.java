import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;
import java.security.DigestOutputStream;        // new
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

void downloadFile(String fromURL, String toFile, BigInteger md5)
    throws IOException, NoSuchAlgorithmException
{
    ReadableByteChannel in = Channels.newChannel(new URL(fromURL).openStream());
    MessageDigest md5Digest = MessageDigest.getInstance("MD5");
    WritableByteChannel out = Channels.newChannel(
        //new FileOutputStream(toFile));  // old
        new DigestOutputStream(new FileOutputStream(toFile), md5Digest));  // new
    ByteBuffer buffer = ByteBuffer.allocate(1024 * 1024);  // 1 MB

    while (in.read(buffer) != -1) {
        buffer.flip();
        //md5Digest.update(buffer.asReadOnlyBuffer());  // old
        out.write(buffer);
        buffer.clear();
    }

    BigInteger md5Actual = new BigInteger(1, md5Digest.digest()); 
    if (! md5Actual.equals(md5))
        throw new RuntimeException(
            "MD5 mismatch for file " + toFile +
            ": expected " + md5.toString(16) +
            ", got " + md5Actual.toString(16)
        );
}
