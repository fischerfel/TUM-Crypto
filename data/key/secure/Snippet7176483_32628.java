import static org.apache.commons.codec.binary.Hex.*;
import static org.apache.commons.io.FileUtils.*;
import java.io.*;
import java.security.NoSuchAlgorithmException;
import javax.crypto.*;
import org.apache.commons.codec.DecoderException;

public static SecretKey generateKey() throws NoSuchAlgorithmException
{
    KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
    keyGenerator.init(256); // 128 default; 192 and 256 also possible
    return keyGenerator.generateKey();
}

public static void saveKey(SecretKey key, File file) throws IOException
{
    char[] hex = encodeHex(key.getEncoded());
    writeStringToFile(file, String.valueOf(hex));
}

public static SecretKey loadKey(File file) throws IOException
{
    String data = new String(readFileToByteArray(file));
    byte[] encoded;
    try {
        encoded = decodeHex(data.toCharArray());
    } catch (DecoderException e) {
        e.printStackTrace();
        return null;
    }
    return new SecretKeySpec(encoded, "AES");
}
