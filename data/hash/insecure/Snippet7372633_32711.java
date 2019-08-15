import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import sun.misc.BASE64Encoder;

public class MD5 {

    static char[] carr = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };

    public static String getBase64FromHEX(String input) {

        byte barr[] = new byte[16];
        int bcnt = 0;
        for (int i = 0; i < 32; i += 2) {
            char c1 = input.charAt(i);
            char c2 = input.charAt(i + 1);
            int i1 = intFromChar(c1);
            int i2 = intFromChar(c2);

            barr[bcnt] = 0;
            barr[bcnt] |= (byte) ((i1 & 0x0F) << 4);
            barr[bcnt] |= (byte) (i2 & 0x0F);
            bcnt++;
        }

        BASE64Encoder encoder = new BASE64Encoder();
        return encoder.encode(barr);
    }

    public static synchronized String getMD5_Base64(String input) {
        // please note that we dont use digest, because if we
        // cannot get digest, then the second time we have to call it
        // again, which will fail again
        MessageDigest digest = null;

        try {
            digest = MessageDigest.getInstance("MD5");
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        if (digest == null)
            return input;

        // now everything is ok, go ahead
        try {
            digest.update(input.getBytes("UTF-8"));
        } catch (java.io.UnsupportedEncodingException ex) {
            ex.printStackTrace();
        }
        byte[] rawData = digest.digest();
        BASE64Encoder bencoder = new BASE64Encoder();
        return bencoder.encode(rawData);
    }

    private static int intFromChar(char c) {
        char clower = Character.toLowerCase(c);
        for (int i = 0; i < carr.length; i++) {
            if (clower == carr[i]) {
                return i;
            }
        }

        return 0;
    }

    public static void main(String[] args) {

        //String password = args[0];
        String password = "test";

        MessageDigest digest = null;

        try {
            digest = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        try {
            digest.update(password.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException ex) {
            ex.printStackTrace();
        }

        byte[] rawData = digest.digest();
        StringBuffer printable = new StringBuffer();

        for (int i = 0; i < rawData.length; i++) {
            printable.append(carr[((rawData[i] & 0xF0) >> 4)]);
            printable.append(carr[(rawData[i] & 0x0F)]);
        }
        String phpbbPassword = printable.toString();

        System.out.println("PHPBB           : " + phpbbPassword);
        System.out.println("MVNFORUM        : " + getMD5_Base64(password));
        System.out.println("PHPBB->MVNFORUM : " + getBase64FromHEX(phpbbPassword));
    }

}
