    import java.io.BufferedWriter;
import java.io.IOException;
import java.security.MessageDigest;

public class ShortCuts {
volatile static long  MD5TIME = 1, SHA1TIME = 2;
    public static String MD5(String message,BufferedWriter bw) {
        try {
            long start = System.nanoTime();
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.update(message.getBytes());
            byte[] hashBytes = md5.digest();

            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < hashBytes.length; i++) {
                sb.append(Integer.toString((hashBytes[i] & 0xff) + 0x100, 16)
                        .substring(1));
            }
            long end = System.nanoTime();
            MD5TIME = end - start;
            System.out.println(MD5TIME);
            bw.newLine();
            System.out.println(MD5TIME);
            bw.write("TIME: " + MD5TIME);
            bw.newLine();
            return sb.toString();

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String SHA(String message, BufferedWriter bw) throws IOException {
        long start = System.nanoTime();
        int[] t = SHA1.prepareDataForSHA1(message);
        SHA1TIME = System.nanoTime() - start;
        bw.newLine();
        bw.write("TIME: " + SHA1TIME);
        bw.newLine();
        return SHA1.doSHA1(t);
    }

    public static void addShortcutsIntoTheFile(BufferedWriter bw, String message) {
        try {
            bw.newLine();
            bw.write("MD5");

            bw.write(ShortCuts.MD5(message,bw));
            bw.newLine();
            bw.newLine();
            bw.write("SHA");
            bw.write(ShortCuts.SHA(message,bw));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
