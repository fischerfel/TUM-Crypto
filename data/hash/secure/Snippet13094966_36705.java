import java.io.*;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.security.MessageDigest;
import org.bouncycastle.util.encoders.Hex;

public class MyTest {

    public static void main(String[] args) throws Exception {

        // 1st step:
        // ------------------------------------------------
        byte[] data = openFile();

        // Create file to write
        FileOutputStream fos = new FileOutputStream(new File("test"));
        ObjectOutputStream oosf = new ObjectOutputStream(fos);
        // Write byte[]-length and byte[]
        oosf.writeInt(data.length);
        oosf.write(data);

        // Flush & Close
        fos.flush();
        fos.close();

        // Print hash value of saved byte[]
        try {
            final MessageDigest messageDigest = MessageDigest.getInstance("SHA-512");
            messageDigest.reset();
            System.out.println(new String(Hex.encode(messageDigest.digest(data))));
        } catch (Exception e) {
        }

        // 2nd step
        // ------------------------------------------------

        // Open just saved file
        FileInputStream fis = new FileInputStream(new File("test"));
        ObjectInputStream ois = new ObjectInputStream(fis);

        // Read the length and create a byte[]
        int length = ois.readInt();
        byte[] dataRead = new byte[length];
        // Read the byte[] itself
        ois.read(dataRead);

        // Print hash value of read byte[]
        try {
            final MessageDigest messageDigest = MessageDigest.getInstance("SHA-512");
            messageDigest.reset();
            System.out.println(new String(Hex.encode(messageDigest.digest(dataRead))));
        } catch (Exception e) {
        }

        // Both printed hash values should be the same

    }

    private static byte[] openFile() throws Exception {
        // Download a sample file which will be converted to a byte[]
        URL website = new URL("http://www.marcel-carle.de/assets/Cryptonify/Cryptonify-1.7.8.zip");
        ReadableByteChannel rbc = Channels.newChannel(website.openStream());
        FileOutputStream fos2 = new FileOutputStream("tmp");
        fos2.getChannel().transferFrom(rbc, 0, 1 << 24);
        fos2.flush();
        fos2.close();

        // Open downloaded file and convert to byte[]
        File selectedFile = new File("tmp");
        FileInputStream fis1 = new FileInputStream(selectedFile);
        byte[] data = new byte[(int) selectedFile.length()];
        fis1.read(data);
        fis1.close();


        return data;
    }
}
