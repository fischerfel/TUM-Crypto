import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.xml.bind.DatatypeConverter;

public class Checksum {

    /**
     * Generates an MD5 checksum as a String.
     * @param file The file that is being checksummed.
     * @return Hex string of the checksum value.
     * @throws NoSuchAlgorithmException
     * @throws IOException
     */
    public static String generate(File file) throws NoSuchAlgorithmException,IOException {

        MessageDigest messageDigest = MessageDigest.getInstance("MD5");
        messageDigest.update(Files.readAllBytes(file.toPath()));
        byte[] hash = messageDigest.digest();

        return DatatypeConverter.printHexBinary(hash).toUpperCase();
    }

    public static void main(String argv[]) throws NoSuchAlgorithmException, IOException {
        File file = new File("/Users/foo.bar/Documents/file.jar");          
        String hex = Checksum.generate(file);
        System.out.printf("hex=%s\n", hex);            
    }


}
