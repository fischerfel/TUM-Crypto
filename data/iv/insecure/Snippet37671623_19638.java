import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;



public class EncDec {

    public static void main(String args[])
    {


        String reqMessage="{\"accountType\":\"ALL\",\"uId\":\"c8ff46be-a083-4009-8a33-fc2d22cc40e3|123456784\",\"deviceId\":\"qvxy1234\"}";
        Map requestMap=new HashMap();
        requestMap.put("body", reqMessage);
        String bodyString=(String) requestMap.get("body");
        String authKey="M/98hZivBqJQftMHsPvMgg&&";

         String encString= encode(authKey,bodyString);
         System.out.println("encString ::: "+ encString);

         String decString= decode(authKey,encString);  
         System.out.println("decString ::: "+ decString);



    }
    public static String encode(String keyString, String stringToEncode) throws NullPointerException {
        if (keyString.length() == 0 || keyString == null) {
            throw new NullPointerException("Please give Password");
        }

        if (stringToEncode.length() == 0 || stringToEncode == null) {
            throw new NullPointerException("Please give text");
        }

        try {
            SecretKeySpec skeySpec = getKey(keyString);
            byte[] clearText = stringToEncode.getBytes("UTF8");

            // IMPORTANT TO GET SAME RESULTS ON iOS and ANDROID
            final byte[] iv = new byte[16];
            Arrays.fill(iv, (byte) 0x00);
            IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);

            // Cipher is not thread safe
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec, ivParameterSpec);
            byte[] encryptedByte=cipher.doFinal(clearText); 
            String encrypedValue = new String(Base64.encodeBase64(encryptedByte));
           System.out.println("Encrypted: " + stringToEncode + " -> " + encrypedValue);
            return encrypedValue;

        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * Decodes a String using AES-128 and Base64
     *
     * @param context
     * @param password
     * @param text
     * @return desoded String
     */
    public static String decode(String password, String text) throws NullPointerException {

        if (password.length() == 0 || password == null) {
            throw new NullPointerException("Please give Password");
        }

        if (text.length() == 0 || text == null) {
            throw new NullPointerException("Please give text");
        }

        try {
            SecretKey key = getKey(password);

            // IMPORTANT TO GET SAME RESULTS ON iOS and ANDROID
            final byte[] iv = new byte[16];
            Arrays.fill(iv, (byte) 0x00);
            IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);

            byte[] encrypedPwdBytes = Base64.decodeBase64(text.getBytes());
            // cipher is not thread safe
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            cipher.init(Cipher.DECRYPT_MODE, key, ivParameterSpec);
            byte[] decrypedValueBytes = (cipher.doFinal(encrypedPwdBytes));

            String decrypedValue = new String(decrypedValueBytes);
            System.out.println("Decrypted: " + text + " -> " + decrypedValue);
            return decrypedValue;

        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * Generates a SecretKeySpec for given password
     *
     * @param password
     * @return SecretKeySpec
     * @throws UnsupportedEncodingException
     */
    private static SecretKeySpec getKey(String password) throws UnsupportedEncodingException {

        // You can change it to 256 if you wish
        int keyLength = 128;
        byte[] keyBytes = new byte[keyLength / 8];
        // explicitly fill with zeros
        Arrays.fill(keyBytes, (byte) 0x0);

        // if password is shorter then key length, it will be zero-padded
        // to key length
        byte[] passwordBytes = password.getBytes("UTF-8");
        int length = passwordBytes.length < keyBytes.length ? passwordBytes.length : keyBytes.length;
        System.arraycopy(passwordBytes, 0, keyBytes, 0, length);
        SecretKeySpec key = new SecretKeySpec(keyBytes, "AES");
        return key;
    }


}
