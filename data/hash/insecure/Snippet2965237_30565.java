import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Test {

    public static void main(String[] args) throws NoSuchAlgorithmException,
            IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);

        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            SomeObject testObject = new SomeObject();

            dos.writeInt(testObject.count);
            dos.writeLong(testObject.product);
            dos.writeDouble(testObject.stdDev);
            dos.writeUTF(testObject.name);
            dos.writeChar(testObject.delimiter);
            dos.flush();

            byte[] hashBytes = md.digest(baos.toByteArray());
            BigInteger testObjectHash = new BigInteger(hashBytes);

            System.out.println("Hash " + testObjectHash);
        } finally {
            dos.close();
        }
    }

    private static class SomeObject {
        private int count = 200;
        private long product = 1235134123l;
        private double stdDev = 12343521.456d;
        private String name = "Test Name";
        private char delimiter = '\n';
    }
}
