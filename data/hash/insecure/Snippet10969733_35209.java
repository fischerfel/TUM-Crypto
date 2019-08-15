import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.nio.ByteBuffer;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

public class HashKeyTest implements Serializable{

    long time;
    String str;

    public HashKeyTest(String str, long time) {
        this.time = time;
        this.str = str;
    }

    public double random() throws IOException, NoSuchAlgorithmException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream out = new ObjectOutputStream(bos);
        out.writeObject(this);
        byte[] bytes = bos.toByteArray();
        MessageDigest md5Digest = MessageDigest.getInstance("MD5");
        byte[] hash = md5Digest.digest(bytes);
        ByteBuffer bb = ByteBuffer.wrap(hash);
        long seed = bb.getLong();

        return new Random(seed).nextDouble();
    }

    public static void main(String[] args) throws Exception {
        long time = 0;
        for (int i = 0; i < 10; i++) {
            time += 250L;
            HashKeyTest hk = new HashKeyTest("SPY", time);
            System.out.format("%d:%10.12f\n", time, hk.random());
            Thread.sleep(1);
        }
    }
}
