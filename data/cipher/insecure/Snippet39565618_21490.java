import java.security.Key;
import java.security.MessageDigest;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;


public class H {
    public static void main(String args[]){
        try{
        String strDefaultKey = "QabC-+50";
        Key key = new SecretKeySpec(strDefaultKey.getBytes("UTF-8"), "DES");
        Cipher encryptCipher = Cipher.getInstance("DES/ECB/NoPadding");
        encryptCipher.init(Cipher.ENCRYPT_MODE, key);
        String seed = "2016-09-19 05:11";
        String seedMd5 = MD5(seed);
        byte[] a = encryptCipher.doFinal(seedMd5.getBytes());
        byte[] encodeUrl = Base64.getEncoder().encode(a);
        String finalUrl = new String(encodeUrl);
        String finalResult = finalUrl.substring(2, 8) + finalUrl.substring(10, 13);
        System.out.println(finalUrl);
        System.out.println(finalResult);
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    static String MD5(String src) {
        MessageDigest md;
        try {
            md = MessageDigest.getInstance("MD5");
            StringBuffer deviceIDString = new StringBuffer(src);
            src = convertToHex(md.digest(deviceIDString.toString().getBytes()));
        } catch (Exception e) {
            src = "00000000000000000000000000000000";
        }
        return src;
    }

    private static String convertToHex(byte[] data) {
        StringBuffer buf = new StringBuffer();
        for (int i = 0; i < data.length; i++) {
            int halfbyte = (data[i] >>> 4) & 0x0F;
            int two_halfs = 0;
            do {
                if ((0 <= halfbyte) && (halfbyte <= 9))
                    buf.append((char) ('0' + halfbyte));
                else
                    buf.append((char) ('a' + (halfbyte - 10)));
                halfbyte = data[i] & 0x0F;
            } while (two_halfs++ < 1);
        }
        return buf.toString();
    }
}
