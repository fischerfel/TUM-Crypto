import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;

public class Encryption {

    private static SecretKey sharedkey;
    private static byte [] sharedvector;

    static {
        int keySize = 168;
        int ivSize = 8;
        try {
            KeyGenerator keyGenerator = KeyGenerator.getInstance("DESede");
            keyGenerator.init(keySize);
            sharedkey = keyGenerator.generateKey();

            sharedvector = new byte [ivSize];
            byte [] data = sharedkey.getEncoded();

            int half = ivSize / 2;
            System.arraycopy(data, data.length-half, sharedvector, 0, half);
            System.arraycopy(sharedvector, 0, sharedvector, half, half);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    public static void main(String [] args) throws Exception {
        System.out.println(Decrypt(Encrypt("Hello World")));

    }

    public static String Encrypt(String val) throws GeneralSecurityException {
        Cipher cipher = Cipher.getInstance("DESede/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, sharedkey, new IvParameterSpec(sharedvector));

        return new sun.misc.BASE64Encoder().encode(cipher.doFinal(val.getBytes()));
    }

    public static String Decrypt(String val) throws GeneralSecurityException, IOException {
        Cipher cipher = Cipher.getInstance("DESede/CBC/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, sharedkey, new IvParameterSpec(sharedvector));

        return new String(cipher.doFinal(new sun.misc.BASE64Decoder().decodeBuffer(val)));
    }

}
