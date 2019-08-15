import javax.crypto.*;
import javax.crypto.spec.*;
import java.security.SecureRandom;
import java.util.Scanner;
import java.security.spec.*;
import java.security.AlgorithmParameters;
import javax.crypto.SecretKeyFactory.*;

class AES
{
    static public String encrypt(String input, String password)
    {
        SecureRandom random = new SecureRandom();
        byte salt[] = new byte[8];
        random.nextBytes(salt);

        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, 65536, 256);
        SecretKey tmp = factory.generateSecret(spec);
        SecretKey secret = new SecretKeySpec(tmp.getEncoded(), "AES");

        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, secret);
        AlgorithmParameters params = cipher.getParameters();
        byte[] iv = params.getParameterSpec(IvParameterSpec.class).getIV();
        byte[] ciphertext = cipher.doFinal(input.getBytes("UTF-8"));

        String text = new String(ciphertext, "UTF-8");
        return text;
    }
}
