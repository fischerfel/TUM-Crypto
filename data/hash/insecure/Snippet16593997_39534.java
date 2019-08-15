package my.package;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.security.DigestInputStream;
import java.security.MessageDigest;

public class SHA1Encrypt {

    public static void main(String[] args) throws Exception {

        String str = "Content-Type: plain/text; name=\"C:\\Users\\Luigi\\Desktop\\hello.txt\"" + "\n" +
                  "Content-Disposition: attachment; filename=\"C:\\Users\\Luigi\\Desktop\\hello.txt\"" + "\n" +
                  "test";

        String SHA1FromString = getSHA1FromString(str);
        String SHA1FromIS = getSHA1FromIS(str);

        System.out.println("SHA1 from String is: " + SHA1FromString.toUpperCase());
        System.out.println("SHA1 from InputStream is: " + SHA1FromIS.toUpperCase());

    }

    public static String getSHA1FromString(String str) throws Exception {
        MessageDigest sha1 = MessageDigest.getInstance("SHA1");
        sha1.update(str.getBytes());
        byte[] digest = sha1.digest();
        return byteArrayToHexString(digest);
    }

    public static String getSHA1FromIS(String str) throws Exception{

        MessageDigest sha1 = MessageDigest.getInstance("SHA1");

        InputStream is = new ByteArrayInputStream(str.getBytes());
        BufferedInputStream bis = new BufferedInputStream(is);
        DigestInputStream   dis = new DigestInputStream(bis, sha1);

        while (dis.read() != -1); 
        byte[] digest = sha1.digest();
        return byteArrayToHexString(digest);
    }

    public static String byteArrayToHexString(byte[] b) {
        String result = "";
        for (int i=0; i < b.length; i++) {
          result += Integer.toString((b[i] & 0xff) + 0x100, 16).substring(1);
        }
        return result;
    }

}
