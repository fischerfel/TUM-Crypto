import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

public class Project2
{
    public static void main(String[] args)
    {
        try
        {
            ByteBuffer buffer = ByteBuffer.allocate(4);
            buffer.putInt(0xAABBCCDD); //Test value
            byte[] digest = MessageDigest.getInstance("SHA-1").digest(buffer.array());
            BigInteger bi = new BigInteger(1, digest);

            //Big Integer output:
            System.out.println(bi.toString(16));
            System.out.println("");

            //Byte array output:
            for(byte b : digest)
            {
                System.out.println(Integer.toHexString(b));
            }
        }
        catch (NoSuchAlgorithmException e)
        {
            e.printStackTrace();
        }
    }
}
