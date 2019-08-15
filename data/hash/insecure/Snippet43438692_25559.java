import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

public class Main {

    public static final int BLOCKSIZE = 8*1024;

    public static void main(String[] args) throws FileNotFoundException, NoSuchAlgorithmException{
        String path = Main.class.getResource("file5M.img").getPath();
        File file = new File(path);
        FileInputStream fin = new FileInputStream(file);
        MessageDigest messageDigest = MessageDigest.getInstance("MD5");

        long fileSize = file.length();
        int length;
        long alreadyRead = 0;
        long startTime = System.currentTimeMillis();
        byte[] bytes = new byte[BLOCKSIZE];
        try {
            while (true) {
                int maxToRead = (int) (fileSize - alreadyRead < BLOCKSIZE ? fileSize - alreadyRead : BLOCKSIZE);

                if ((length = fin.read(bytes, 0, maxToRead)) < 0) break;
                messageDigest.update(bytes, 0, length);
                if ((alreadyRead += length) >= fileSize) break;
            }
        } catch (IOException ex){
            ex.printStackTrace();
        }
        byte[] md5 = messageDigest.digest();
        long stopTime = System.currentTimeMillis();
        long elapsedTime = stopTime - startTime;
        System.out.println("Time:\t" + elapsedTime + "\tRead:\t" + alreadyRead/1024/1024);
        System.out.println("MD5: " + Arrays.toString(md5));
    }
}
