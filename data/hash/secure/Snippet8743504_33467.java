import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class BenchmarkSha256 {

  public static void main(String... args) throws NoSuchAlgorithmException {
    int size = 1024 * 1024;
    byte[] bytes = new byte[size];
    MessageDigest md = MessageDigest.getInstance("SHA-256");
    long startTime = System.nanoTime();
    for (int i = 0; i < 1024; i++)
      md.update(bytes, 0, size);
    long endTime = System.nanoTime();
    System.out.println(String.format("%1$064x", new java.math.BigInteger(1, md.digest())));
    System.out.println(String.format("%d ms", (endTime - startTime) / 1000000));
  }

}
