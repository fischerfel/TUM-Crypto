import java.io.FileNotFoundException;
import java.io.RandomAccessFile;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Test {

    public static byte[] extractFileHashSHA256(String filename) throws Exception {

        byte[] partialHash = null;
        int buff = 16384;
        try {
            RandomAccessFile file = new RandomAccessFile(filename, "r");

            MessageDigest hashSum = MessageDigest.getInstance("SHA-256");

            byte[] buffer = new byte[buff];

            long read = 0;

            // calculate the hash of the hole file for the test
            long offset = file.length();
            int unitsize;
            while (read < offset) {
                unitsize = (int) (((offset - read) >= buff) ? buff : (offset - read));
                file.read(buffer, 0, unitsize);

                hashSum.update(buffer, 0, unitsize);

                read += unitsize;
            }

            file.close();
            partialHash = new byte[hashSum.getDigestLength()];
            partialHash = hashSum.digest();


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return partialHash;
    }

    public static void main(String[] args)  throws Exception {
        String output =  new java.math.BigInteger(extractFileHashSHA256("a.pdf")).toString(16);
        System.out.println(output);

        return ;
    }   
}
