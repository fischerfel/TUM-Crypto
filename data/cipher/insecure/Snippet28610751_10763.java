import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.security.Key;
import java.util.Arrays;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;

public class TripleDes3 {
    private Cipher cipher = null;
    private SecretKey key = null;
    private byte[] bytes = null;
    private IvParameterSpec iv = null;

    public static void main(String[] args) throws Exception {
        try {
            String hexKey = "GD6GTT56HKY4HGF6FH3JG9J5";
            //TripleDes3 encryptor = new TripleDes3(new String(Hex.decodeHex(hexKey.toCharArray())));
            TripleDes3 encryptor = new TripleDes3(hexKey);
            String original = "ABC";
            System.out.println("Oringal: \"" + original + "\"");

            String enc = encryptor.encrypt(original);
            System.out.println("Encrypted: \"" + enc.toUpperCase() + "\"");

            String dec = encryptor.decrypt(enc);
            System.out.println("Decrypted: \"" + dec.toUpperCase() + "\"");

            if (dec.equals(original)) {
                System.out.println("Encryption ==> Decryption Successful");
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.toString());
        }
    }

    public TripleDes3(String encryptionKey) throws GeneralSecurityException, DecoderException {
        cipher = Cipher.getInstance("DESede/CBC/NoPadding");
        try {
            key = new SecretKeySpec(encryptionKey.getBytes("ISO8859_15"), "DESede");
            iv = new IvParameterSpec(Hex.decodeHex("0123456789abcdef".toCharArray()));

        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public String encrypt(String input) throws GeneralSecurityException, UnsupportedEncodingException {
        bytes = input.getBytes("ISO8859_15");
        bytes = Arrays.copyOf(bytes, ((bytes.length+7)/8)*8);
        return new String(Hex.encodeHex(encryptB(bytes)));
    }

    public String decrypt(String input) throws GeneralSecurityException, DecoderException, UnsupportedEncodingException {
        bytes = Hex.decodeHex(input.toCharArray());
        String decrypted = new String(decryptB(bytes), "ISO8859_15");
        if (decrypted.indexOf((char) 0) > 0) {
            decrypted = decrypted.substring(0, decrypted.indexOf((char) 0));
        }
        return decrypted;
    }

    public byte[] encryptB(byte[] bytes) throws GeneralSecurityException {
        cipher.init(Cipher.ENCRYPT_MODE, (Key) key, iv);
        return cipher.doFinal(bytes);
    }

    public byte[] decryptB(byte[] bytes) throws GeneralSecurityException {
        cipher.init(Cipher.DECRYPT_MODE, (Key) key, iv);
        return cipher.doFinal(bytes);
    }
}
