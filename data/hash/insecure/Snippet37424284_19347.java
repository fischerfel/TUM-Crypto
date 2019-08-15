import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Test{ 

   public static void main(String args[]){

        byte[] k1 = parseHexString("eb35a6c92c3b8c98033d739969fcc1f5ee08549e", 20);
        byte[] k2 = parseHexString("57cb8b13a1f654de21104c551c13d8820b4d6de3", 20);
        byte[] k3 = parseHexString("c4c4df2f8ad3683677f9667d789f94c7cffb5f39", 20);

      System.out.println(k1);
      System.out.println(k2);
      System.out.println(k3);
      System.out.println(xor(m16h(add(xor(xor(m16h(add(k1, m16h(add(k2, m16h(k3))))), k3), k2), k1)), k3));

   }

    public static byte[] m16h(byte[] m) throws Exception {
        return parseHexString(SHA1(m), 20);
    }

   private static byte[] xor(byte[] x, byte[] y) {
        int l = x.length;
        if (l != y.length) {
            return null;
        }
        byte[] ob = new byte[l];
        for (int i = 0; i < l; i++) {
            ob[i] = (byte) (x[i] ^ y[i]);
        }
        return ob;
    }

    public static byte[] parseHexString(String x, int len) {
        byte[] ret = new byte[len];
        for (int i = 0; i < len; i++) {
            ret[i] = (byte) Integer.parseInt(x.substring(i * 2, (i * 2) + 2), 16);
        }
        return ret;
    }



    public static byte[] add(byte[] x, byte[] y) {
        byte[] added = new byte[(x.length + y.length)];
        System.arraycopy(x, 0, added, 0, x.length);
        System.arraycopy(y, 0, added, x.length, y.length);
        return added;
    }



    public static String SHA1(byte[] c) throws NoSuchAlgorithmException {
        return base16encode(MessageDigest.getInstance("SHA-1").digest(c));
    }

    public static String base16encode(byte[] data) {
        String res = "";
        for (byte b : data) {
            res = String.format("%s%02x", new Object[]{res, Byte.valueOf(b)});
        }
        return res;
    }
}
