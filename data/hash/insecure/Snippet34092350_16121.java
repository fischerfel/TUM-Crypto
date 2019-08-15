import java.io.*;
import java.security.MessageDigest;

public class PrintChecksums {

    public static void main(String[] args) {
        String sourceDir = "/Users/Jan/Desktop/Folder1";
        try {
            new PrintChecksums().printHashs(new File(sourceDir));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void printHashs(File sourceDir) throws Exception {
        for (File f : sourceDir.listFiles()) {
            String hash = createHash(f); // That you almost have
            System.out.println(f.getAbsolutePath() + " / Hashvalue: " + hash);
        }
    }

    public String createHash(File datafile) throws Exception {
        // SNIP - YOUR CODE BEGINS
        MessageDigest md = MessageDigest.getInstance("SHA1");
        FileInputStream fis = new FileInputStream(datafile);
        byte[] dataBytes = new byte[1024];

        int nread = 0;

        while ((nread = fis.read(dataBytes)) != -1) {
            md.update(dataBytes, 0, nread);
        }

        byte[] mdbytes = md.digest();

        // convert the byte to hex format
        StringBuffer sb = new StringBuffer("");
        for (int i = 0; i < mdbytes.length; i++) {
            sb.append(Integer.toString((mdbytes[i] & 0xff) + 0x100, 16).substring(1));
        }
        // SNAP - YOUR CODE ENDS
        return sb.toString();
    }

}
