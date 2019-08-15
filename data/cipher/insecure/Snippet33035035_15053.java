import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

public class AES
{
public static SecretKeySpec makeKey(String schlüssel) throws NoSuchAlgorithmException, UnsupportedEncodingException
{
    byte[] key = (schlüssel).getBytes("UTF-8");
    MessageDigest sha = MessageDigest.getInstance("SHA");
    key = sha.digest(key);
    key = Arrays.copyOf(key, 16);
    return new SecretKeySpec(key, "AES");
}


public static String encryptString(String text, SecretKeySpec schlüssel) throws Exception
{
    Cipher cipher = Cipher.getInstance("AES");
    cipher.init(Cipher.ENCRYPT_MODE, schlüssel);
    byte[] encrypted = cipher.doFinal(text.getBytes());

    BASE64Encoder myEncoder = new BASE64Encoder();
    return myEncoder.encode(encrypted);
}


public static String decryptString(String text, SecretKeySpec schlüssel) throws Exception
{    
    BASE64Decoder myDecoder2 = new BASE64Decoder();
    byte[] crypted2 = myDecoder2.decodeBuffer(text);

    Cipher cipher2 = Cipher.getInstance("AES");
    cipher2.init(Cipher.DECRYPT_MODE, schlüssel);
    byte[] cipherData2 = cipher2.doFinal(crypted2);
    return new String(cipherData2);
}
}
