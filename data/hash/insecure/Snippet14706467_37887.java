import java.io.*;
import java.security.*;

public class DigestThread extends Thread {
    private File input;

    public DigestThread(File input) {
        this.input = input;
    }

    public void run() {
        try {
            FileInputStream in = new FileInputStream(input);
            MessageDigest sha = MessageDigest.getInstance("SHA");
            DigestInputStream din = new DigestInputStream(in, sha);
            int b;
            while ((b = din.read()) != -1)
                ;
            din.close();
            byte[] digest = sha.digest();
            StringBuffer result = new StringBuffer(input.toString());
            result.append(": ");
            for (int i = 0; i < digest.length; i++) {
                result.append(digest[i] + " ");
            }
            System.out.println(result);
        }
        catch (IOException e) {
            System.err.println(e);
        }
        catch (NoSuchAlgorithmException e) {
            System.err.println(e);
        }
    }

    public static void main(String[] args) {
        for (int i = 0; i < args.length; i++) {
            File f = new File(args[i]);
            Thread t = new DigestThread(f);
            t.start();
        }
    }
