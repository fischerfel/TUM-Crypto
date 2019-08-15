import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class main {

public static void main(String[] args) throws IOException {

    byte[] fileContents = new byte[15 * 10000000];

    FileOutputStream out = new FileOutputStream("C:\\testFile");
    out.write(fileContents);
    out.close();

    File file = new File("C:\\testFile");
    FileInputStream fs = new FileInputStream(file);

    System.out.println(new String(getBytesOfMd5(fs)));

}

public static byte[] getBytesOfMd5(InputStream is) throws IOException {
    byte[] buffer = new byte[1024];
    MessageDigest complete = null;
    try {
        complete = MessageDigest.getInstance("MD5");
    } catch (NoSuchAlgorithmException e) {
        return null;
    }

    int numRead;
    do {
        numRead = is.read(buffer);
        if (numRead > 0) {
            complete.update(buffer, 0, numRead);
        }
    } while (numRead != -1);

    is.close();
    return complete.digest();
}
}
