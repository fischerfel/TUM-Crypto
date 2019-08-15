import javax.crypto.*;
import javax.crypto.spec.*;

public class etest {  
    public static void main(String[] args) {
        // TODO Auto-generated method stub
        encryp ec=new encryp();
        String name="1234567890123456";
        System.out.println(name);
        try {
            System.out.println(ec.encrypt(name));
//          System.out.println(ec.decrypt(ec.encrypt(name)));
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}

class encryp {
//  public static String key = "abcdefgh12345678";
    static byte []key=new byte[]{0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00};
    public static String encrypt(String message) throws Exception {
        SecretKeySpec skeySpec = new SecretKeySpec(key, "AES");
        Cipher cipher = Cipher.getInstance("AES/ECB/NOPADDING");
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
        byte[] encrypted = cipher.doFinal(message.getBytes());
        return byteArrayToHex(encrypted);
//      return encrypted;
    }
    public static String decrypt(String encrypted) throws Exception {

        // use key coss2
        SecretKeySpec skeySpec = new SecretKeySpec(key, "AES");

        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, skeySpec);
        byte[] original = cipher.doFinal(hexToByteArray(encrypted));
        String originalString = new String(original);
        return originalString;
    }
    public static byte[] hexToByteArray(String hex) {
        if (hex == null || hex.length() == 0) {
            return null;
        }

        byte[] ba = new byte[hex.length() / 2];
        for (int i = 0; i < ba.length; i++) {
            ba[i] = (byte) Integer
                    .parseInt(hex.substring(2 * i, 2 * i + 2), 16);
        }
        return ba;
    }

    /**
     * 
     * @param ba
     *            byte[]
     * @return
     */
    public static String byteArrayToHex(byte[] ba) {
        if (ba == null || ba.length == 0) {
            return null;
        }

        StringBuffer sb = new StringBuffer(ba.length * 2);
        String hexNumber;
        for (int x = 0; x < ba.length; x++) {
            hexNumber = "0" + Integer.toHexString(0xff & ba[x]);

            sb.append(hexNumber.substring(hexNumber.length() - 2));
        }
        return sb.toString();
    }
}
