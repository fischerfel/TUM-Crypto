import java.security.*;

public class HX {

  final String seed;
  final MessageDigest md;
  final long hash;

  public HX(String s) throws NoSuchAlgorithmException {

   seed = s;
    md = MessageDigest.getInstance("MD5");
    hash = computeHash();
  }

  public long getHash() {
    return hash;
  }

  public long computeHash() {
    md.reset();
    final byte[] digest = md.digest(seed.getBytes());
    return (computeHash(digest, 0) ^ computeHash(digest, 8));
  }

  private static final long computeHash(final byte[] array, final int offset) {
    long value = 0;
    for (int i = 0; i < 8; i++) {
       value = ((value << 8) | (array[offset+i] & 0xFF));
    }
    return value;
   }
}
