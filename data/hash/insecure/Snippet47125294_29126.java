import java.io.*;
import java.security.MessageDigest;

public class MD5Checksum
{
    private static byte[] createChecksum(String filename) throws Exception
    {
        try (InputStream fileInputStream = new FileInputStream(filename))
        {
            byte[] buffer = new byte[1024];
            MessageDigest complete = MessageDigest.getInstance("MD5");
            int numRead;

            do
            {
                numRead = fileInputStream.read(buffer);
                if (numRead > 0)
                {
                    complete.update(buffer, 0, numRead);
                }
            } while (numRead != -1);

            return complete.digest();
        }
    }

    public static String getMD5Checksum(String filename) throws Exception
    {
        byte[] checksum = createChecksum(filename);
        StringBuilder result = new StringBuilder();

        for (byte singleByte : checksum)
        {
            result.append(Integer.toString((singleByte & 0xff) + 0x100, 16).substring(1));
        }

        return result.toString();
    }
}
