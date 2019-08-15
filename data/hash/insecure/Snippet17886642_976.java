import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import junit.framework.Assert;

import org.junit.Test;

public class SplitFile {

    @Test
    public void splitFile() throws IOException, NoSuchAlgorithmException {
        String filename1 = "mp3/Innocence_-_Nero.mp3";
        File file = new File(filename1);
        FileInputStream fis = null;
        FileOutputStream fos = null;
        long filesize = file.length();
        long filesizeActual = 0L;
        int splitval = 5;
        int splitsize = (int)(filesize / splitval) + (int)(filesize % splitval);
        byte[] b = new byte[splitsize];
        System.out.println(filename1 + "            " + filesize + " bytes");
        try {
            fis = new FileInputStream(file);
            String name1 = filename1.replaceAll(".mp3", "");
            String mergeFile = name1 + "_merge.mp3";
            for (int j = 1; j <= splitval; j++) {
                String filecalled = name1 + "_split_" + j + ".mp3";
                fos = new FileOutputStream(filecalled);
                int i = fis.read(b);
                fos.write(b, 0, i);
                fos.close();
                fos = null;
                System.out.println(filecalled + "    " + i + " bytes");
                filesizeActual += i;
            }
            Assert.assertEquals(filesize, filesizeActual);
            mergeFileParts(filename1, splitval);
            check(filename1, mergeFile);
        } finally {
            if(fis != null) {
                fis.close();
            }
            if(fos != null) {
                fos.close();
            }
        }
    }

    private void mergeFileParts(String filename1, int splitval) throws IOException {
        FileInputStream fis = null;
        FileOutputStream fos = null;
        try {
            String name1 = filename1.replaceAll(".mp3", "");
            String mergeFile = name1 + "_merge.mp3";
            fos = new FileOutputStream(mergeFile);
            for (int j = 1; j <= splitval; j++) {
                String filecalled = name1 + "_split_" + j + ".mp3";
                File partFile = new File(filecalled);
                fis = new FileInputStream(partFile);
                int partFilesize = (int) partFile.length();
                byte[] b = new byte[partFilesize];
                int i = fis.read(b, 0, partFilesize);
                fos.write(b, 0, i);
                fis.close();
                fis = null;
            }
        } finally {
            if(fis != null) {
                fis.close();
            }
            if(fos != null) {
                fos.close();
            }
        }
    }

    private void check(String expectedPath, String actualPath) throws IOException, NoSuchAlgorithmException {
        System.out.println("check...");

        FileInputStream fis = null;

        try {

            File expectedFile = new File(expectedPath);
            long expectedSize = expectedFile.length();

            File actualFile = new File(actualPath);
            long actualSize = actualFile.length();

            System.out.println("exp=" + expectedSize);
            System.out.println("act=" + actualSize);

            Assert.assertEquals(expectedSize, actualSize);

            fis = new FileInputStream(expectedFile);
            String expected = makeMessageDigest(fis);
            fis.close();
            fis = null;


            fis = new FileInputStream(actualFile);
            String actual = makeMessageDigest(fis);
            fis.close();
            fis = null;

            System.out.println("exp=" + expected);
            System.out.println("act=" + actual);

            Assert.assertEquals(expected, actual);

        } finally {
            if(fis != null) {
                fis.close();
            }
        }
    }

    public String makeMessageDigest(InputStream is) throws NoSuchAlgorithmException, IOException {
        byte[] data = new byte[1024];
        MessageDigest md = MessageDigest.getInstance("SHA1");
        int bytesRead = 0;

        while(-1 != (bytesRead = is.read(data, 0, 1024))) {
            md.update(data, 0, bytesRead);
        }

        return toHexString(md.digest());
    }

    private String toHexString(byte[] digest) {
        StringBuffer sha1HexString = new StringBuffer();
        for(int i = 0; i < digest.length; i++) {
            sha1HexString.append(String.format("%1$02x", Byte.valueOf(digest[i])));
        }

        return sha1HexString.toString();
    }
}
