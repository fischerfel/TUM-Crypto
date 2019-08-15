import java.io.*;
import java.security.MessageDigest;

public class Checksums {

    public static void main(String[] args) {
        String sourceDir = "/Users/Jan/Desktop/Folder1";
        String targetDir = "/Users/Jan/Desktop/Folder2";
        try {
            new Checksums().createHash(new File(sourceDir), new File(targetDir));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void createHash(File sourceDir, File targetDir) throws Exception {
        for (File f : sourceDir.listFiles()) {
            String hash = createHash(f); // That you almost have
            File target = new File(targetDir, f.getName() + ".hash");
            writeHash(target, hash);
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

    public void writeHash(File target, String hash) {
        try (FileOutputStream fo = new FileOutputStream(target)) {
            fo.write(hash.getBytes());
            System.out.println("Hash written for " + target.getAbsolutePath());
        } catch (IOException e) {
            System.err.println("No Hash Written for " + target.getName());
        }
    }

}
