import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

import org.bouncycastle.util.encoders.Base64;


public class KeyGen {
    private SecretKey key;
    private Cipher ecipher;
    private Cipher dcipher;
    private static KeyGen keyGen;

    private KeyGen() throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException{
        key = KeyGenerator.getInstance("DES").generateKey();
        ecipher = Cipher.getInstance("DES");
        dcipher = Cipher.getInstance("DES");
        ecipher.init(Cipher.ENCRYPT_MODE, key);
        dcipher.init(Cipher.DECRYPT_MODE, key);
    }

    public static KeyGen getInstance() throws NoSuchAlgorithmException, InvalidKeyException, NoSuchPaddingException {
        if(keyGen == null) {
            keyGen = new KeyGen();
        }
        return keyGen;
    }

    public String encrypt(String str) throws UnsupportedEncodingException, IllegalBlockSizeException, BadPaddingException {
        byte[] utf8 = str.getBytes("UTF8");
        byte[] enc = ecipher.doFinal(utf8);
        return new String(Base64.encode(enc));
    }

    public String decrypt(String str) throws IllegalBlockSizeException, BadPaddingException, UnsupportedEncodingException {
        byte[] dec = Base64.decode(str);
        byte[] utf8 = dcipher.doFinal(dec);
        return new String(utf8, "UTF8");
    }

    public static void main(String[] args) throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, UnsupportedEncodingException, IllegalBlockSizeException, BadPaddingException {
        KeyGen keyGen = KeyGen.getInstance();
        String string = "JOYMAA";
        String enc = keyGen.encrypt(string);
        System.out.println(enc);
        String dec = keyGen.decrypt(enc);
        System.out.println(dec);
    }
}
