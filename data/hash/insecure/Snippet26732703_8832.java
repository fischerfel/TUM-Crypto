   import java.security.MessageDigest;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
public class SimpleCrypto {
     public static String encrypt(String seed, String cleartext) throws Exception {
         byte[] rawKey = getRawKey(seed.getBytes("US-ASCII"));
         byte[] result = encrypt(rawKey, cleartext.getBytes());
         return toHex(result);
 }



 private static byte[] getRawKey(byte[] seed) throws Exception {

        MessageDigest md;
        md = MessageDigest.getInstance("MD5");

        // md.update(seed);

         byte[] temp=md.digest(seed);

        byte[] raw =new byte[32];

     System.arraycopy(temp, 0, raw, 0, temp.length);
       System.arraycopy(temp, 0, raw, temp.length, temp.length);



     return raw;
     //    return null;
 }


 private static byte[] encrypt(byte[] raw, byte[] clear) throws Exception {
     SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES/ECB/NoPadding");
         Cipher cipher = Cipher.getInstance("AES/ECB/NoPadding");
     cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
     byte[] encrypted = cipher.doFinal(clear);
         return encrypted;
 }



 public static String toHex(String txt) {
         return toHex(txt.getBytes());
 }
 public static String fromHex(String hex) {
         return new String(toByte(hex));
 }

 public static byte[] toByte(String hexString) {
         int len = hexString.length()/2;
         byte[] result = new byte[len];
         for (int i = 0; i < len; i++)
                 result[i] = Integer.valueOf(hexString.substring(2*i, 2*i+2), 16).byteValue();
         return result;
 }

 public static String toHex(byte[] buf) {
         if (buf == null)
                 return "";
         StringBuffer result = new StringBuffer(2*buf.length);
         for (int i = 0; i < buf.length; i++) {
                 appendHex(result, buf[i]);
         }
         return result.toString();
 }
 private final static String HEX = "0123456789ABCDEF";
 private static void appendHex(StringBuffer sb, byte b) {
         sb.append(HEX.charAt((b>>4)&0x0f)).append(HEX.charAt(b&0x0f));
 }
}
