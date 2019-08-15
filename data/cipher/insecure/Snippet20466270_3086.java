import java.security.NoSuchAlgorithmException;
import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;


public class AESEncoder {

    private SecretKeySpec keyspec;
    private Cipher cipher;
    private String secretkey;

    public AESEncoder(String secretkey) {
        this.secretkey = secretkey;
        keyspec = new SecretKeySpec(secretkey.getBytes(), 0, 16, "AES");

//      keyspec=new SecretKeySpec(key, offset, len, secretkey);

        try {
            cipher = Cipher.getInstance("AES/ECB/NoPadding");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        }
    }
     public byte[] encrypt(String text) throws Exception {
        if (text == null || text.length() == 0) {
            throw new Exception("Empty string");
        }

        int encrypted = 0;

        byte[] bytenc = null;//new byte[32];
        byte[] input = null;

        try {
            cipher.init(Cipher.ENCRYPT_MODE, keyspec);
//            byte empty[]=padString(text).getBytes();
//            encrypted = cipher.doFinal(padString(text).getBytes());
//            encrypted=cipher.doFinal(padString(text).getBytes(), 0, 0, padString(text).getBytes(), 0);

            input = padString(text).getBytes();
            bytenc = new byte[input.length];
            encrypted = cipher.doFinal(input, 0, input.length, bytenc, 0);

            String str = new String(bytenc, 0, encrypted);
//            encrypted=cipher.update(padString(text).getBytes(), 0, 0, 0, 0);
//            System.out.println("Encrypted is:>>" + str);
//            bytenc=hexToBytes(String.valueOf(encrypted));
        } catch (Exception e) {
            throw new Exception("[encrypt] " + e.getMessage());
        }
        return bytenc;
    }


    public String encrypt_hsm(String text) throws Exception {
        if (text == null || text.length() == 0) {
            throw new Exception("Empty string");
        }
        String base64=null;
        int encrypted = 0;

        byte[] bytenc = null;//new byte[32];
        byte[] input = null;

        try {
            cipher.init(Cipher.ENCRYPT_MODE, keyspec);
//            byte empty[]=padString(text).getBytes();
//            encrypted = cipher.doFinal(padString(text).getBytes());
//            encrypted=cipher.doFinal(padString(text).getBytes(), 0, 0, padString(text).getBytes(), 0);

            input = padString(text).getBytes();
            bytenc = new byte[input.length];
            encrypted = cipher.doFinal(input, 0, input.length, bytenc, 0);

            String str = new String(bytenc, 0, encrypted);

            base64 = Base64.encode(bytenc);

//            encrypted=cipher.update(padString(text).getBytes(), 0, 0, 0, 0);
//            System.out.println("Encrypted is:>>" + str);
//            bytenc=hexToBytes(String.valueOf(encrypted));


        } catch (Exception e) {
            throw new Exception("[encrypt] " + e.getMessage());
        }
        return base64;
    }

    public byte[] decrypt(String code) throws Exception {
        if (code == null || code.length() == 0) {
            throw new Exception("Empty string");
        }
        int decrypted = 0;

        byte[] bytedec = null;
        byte[] input = null;

        try {
            cipher.init(Cipher.DECRYPT_MODE, keyspec);

//          input=hexToBytes(code);
            input = Base64ToBytes(code);
            bytedec = new byte[input.length];
            decrypted = cipher.doFinal(input, 0, input.length, bytedec, 0);

            String str = new String(bytedec, 0, decrypted);
//            System.out.println("Decrypted is:>>" + str);

        } catch (Exception e) {
            throw new Exception("[decrypt] " + e.getMessage());
        }
        return bytedec;
    }

    public static String bytesToHex(byte[] bsData) {
        int nDataLen = bsData.length;
        String sHex = "";
        for (int nIter = 0; nIter < nDataLen; nIter++) {
            int nValue = (bsData[nIter] + 256) % 256;
            int nIndex1 = nValue >> 4;
            sHex += Integer.toHexString(nIndex1);
            int nIndex2 = nValue & 0x0f;
            sHex += Integer.toHexString(nIndex2);
        }
        return sHex;
    }

    public static byte[] hexToBytes(String str) {
        if (str == null) {
            return null;
        } else if (str.length() < 2) {
            return null;
        } else {
            int len = str.length() / 2;
            byte[] buffer = new byte[len];
            for (int i = 0; i < len; i++) {
                buffer[i] = (byte) Integer.parseInt(str.substring(i * 2, i * 2 + 2), 16);
            }
            return buffer;
        }
    }

    private static String padString(String source) {
        char paddingChar = ' ';
        int size = 32;
        int x = source.length() % size;
        int padLength = size - x;

        for (int i = 0; i < padLength; i++) {
            source += paddingChar;
        }

//        System.out.println("====>Pad String:" + source);
        return source;
    }

    public void startApp() {
    }

    public void pauseApp() {
    }

    public void destroyApp(boolean unconditional) {
    }

    private byte[] Base64ToBytes(String code) {
        code = code.replace('-', '+');
        code = code.replace('_', '/');
        code = code.replace(',', '=');
        System.out.println("Final Base 64:"+code);

        byte[] aesString = Base64.decode(code);
//        System.out.println("Base64 after decoding:"+new String(aesString));
        return aesString;
    }
}
