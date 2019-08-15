import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public class Main {


    /**
     * entry the content
     *
     * @param content  the content need to entry
     * @param password the key
     * @return
     */
    public static String md5Aessign(String content, String password) {

        MessageDigest md;
        try {

            KeyGenerator kgen = KeyGenerator.getInstance("AES");

            SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
            random.setSeed(password.getBytes());
            kgen.init(128, random);

            SecretKey secretKey = kgen.generateKey();
            byte[] enCodeFormat = secretKey.getEncoded();
            SecretKeySpec key = new SecretKeySpec(enCodeFormat, "AES");
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, key);


            byte[] byteContent = content.getBytes("GBK");

            md = MessageDigest.getInstance("MD5");
            md.update(byteContent);
            byte[] result = cipher.doFinal(md.digest());

            return parseByte2HexStr(result);

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     *
     *
     * @param buf
     * @return
     */

    public static String parseByte2HexStr(byte buf[]) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < buf.length; i++) {
            String hex = Integer.toHexString(buf[i] & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            sb.append(hex.toUpperCase());
        }
        return sb.toString();
    }


    public static void main(String[] args) {
        System.out.println("Hello World!");

        String content = "Hello World!";
        String key = "1234567812345678";

        System.out.println(md5Aessign(content, key));
    }
}
