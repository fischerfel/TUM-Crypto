import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

public class Test7{
    static MessageDigest md; 
    static {
        try {
            md = MessageDigest.getInstance("MD5");
    } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
    }
}

public static void main(String[] args) throws NoSuchAlgorithmException {
    long start = System.nanoTime();
    String md5 = md5("sample test string");
    long time = (System.nanoTime()-start)/1000;
    System.out.println(time);
    System.out.println(md5);
  }
  public static String md5(String stringToDigest) throws NoSuchAlgorithmException {
      if(stringToDigest == null) {
              return "";
      }

      byte[] s = md.digest(stringToDigest.getBytes());
      //HexBinaryAdapter hba = new HexBinaryAdapter();
      return Arrays.toString(s);
      //String md5 = hba.marshal(s);
      //return md5;
  }
}
