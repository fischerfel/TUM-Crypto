import java.io.UnsupportedEncodingException;
import javax.crypto.*;
import java.security.*;
import java.util.Arrays;
import javax.crypto.spec.*;

public class AESCrypto2 {

    private Cipher AEScipher;
    private KeyGenerator AESgen;
    private SecretKeySpec AESkey;
    private SecretKeySpec decodeKey;
    private byte[] cipherData;
    private String msg;

    public static void main(String[] args) {
        try {
            AESCrypto2 a = new AESCrypto2();
            a.encrypt("Hello!");
            a.decrypt(a.getCipherData(), a.getKey());
        } catch (NoSuchAlgorithmException ex) {
            ex.printStackTrace();
        } catch (NoSuchPaddingException ex) {
            ex.printStackTrace();
        } catch (InvalidKeyException ex) {
            ex.printStackTrace();
        } catch (UnsupportedEncodingException ex) {
            ex.printStackTrace();
        } catch (IllegalBlockSizeException ex) {
            ex.printStackTrace();
        } catch (BadPaddingException ex) {
            ex.printStackTrace();
        }

    }

    public AESCrypto2() throws NoSuchAlgorithmException,
            NoSuchPaddingException, UnsupportedEncodingException {
        AESgen = KeyGenerator.getInstance("AES");
        AESgen.init(128);
        AESkey = (SecretKeySpec) AESgen.generateKey();
        decodeKey = new SecretKeySpec(AESkey.getEncoded(), "AES");
        AEScipher = Cipher.getInstance("AES/ECB/NoPadding");
    }

    public AESCrypto2(String msg) throws NoSuchAlgorithmException,
            NoSuchPaddingException, InvalidKeyException,
            UnsupportedEncodingException, IllegalBlockSizeException,
            BadPaddingException {
        this();
        encrypt(msg);
    }

    public byte[] encrypt(String msg) throws NoSuchAlgorithmException,
            InvalidKeyException, UnsupportedEncodingException,
            IllegalBlockSizeException, BadPaddingException,
            NoSuchPaddingException {
        AEScipher.init(Cipher.ENCRYPT_MODE, AESkey);
        cipherData = AEScipher.doFinal(handleString(msg.getBytes("UTF-8")));

        this.msg = msg;
        return cipherData;
    }

    public String decrypt(byte[] enocdedData, SecretKeySpec decodeKey)
            throws InvalidKeyException, IllegalBlockSizeException,
            BadPaddingException, UnsupportedEncodingException,
            NoSuchAlgorithmException, NoSuchPaddingException {
        AEScipher.init(Cipher.DECRYPT_MODE, decodeKey);
        byte[] decryptedData = AEScipher.doFinal(enocdedData);
        String result = new String(decryptedData, "UTF-8");
        System.out.println(result);
        return result;
    }

    public byte[] getCipherData() {
        return cipherData;
    }

    public String getDecryptedMsg() {
        return msg;
    }


    public SecretKeySpec getKey() {
        return decodeKey;
    }

    // AEScipher requires that 16 divides the length of b
    public static byte[] handleString(byte[] b)
            throws UnsupportedEncodingException {
        byte[] temp = b;
        if (temp.length % 16 != 0) {
            byte[] byteMsg = Arrays.copyOf(temp, temp.length + 16
                    - (temp.length % 16));
            return byteMsg;
        }
        return temp;
    }

    public static String byteToHex(byte[] msg) throws UnsupportedEncodingException {
        return Hex.encodeHexString(msg);
    }

    public static byte[] hexToByte(String msg) throws DecoderException {
        return Hex.decodeHex(msg);
    }

}
