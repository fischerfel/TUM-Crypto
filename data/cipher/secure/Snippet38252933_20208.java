import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.SecureRandom;

public class MyTest {

    private static String algorithm = "AES/CBC/PKCS5Padding";

    public static void main(String args[]) throws Exception {
        KeyGenerator keyGen = KeyGenerator.getInstance("AES");
        keyGen.init(256);
        SecretKey secretKey = keyGen.generateKey();

        final int AES_KEYLENGTH = 256;
        byte[] iv = new byte[AES_KEYLENGTH / 16];
        SecureRandom prng = new SecureRandom();
        prng.nextBytes(iv);

        Cipher aesCipherForEncryption = Cipher.getInstance(algorithm);
        Cipher aesCipherForDecryption = Cipher.getInstance(algorithm);
        aesCipherForEncryption.init(Cipher.ENCRYPT_MODE, secretKey, new IvParameterSpec(iv));
        aesCipherForDecryption.init(Cipher.DECRYPT_MODE, secretKey, new IvParameterSpec(iv));

        encryptFile(aesCipherForEncryption, "C:/test/testIn.txt", "C:/test/testOut.txt");
        decryptFile(aesCipherForDecryption, "C:/test/testOut.txt", "C:/test/testOutDecode.txt");
    }


    private static void encryptFile (Cipher cipher, String in, String out) throws Exception {
        crypt(cipher, in, out, true);
    }

    private static void decryptFile (Cipher cipher, String in, String out) throws Exception {
        crypt(cipher, in, out, false);
    }

    private static void crypt (Cipher cipher, String in, String out, boolean encrypt) throws Exception {
        byte[] buffer = new byte[256];
        int numRead;
        InputStream input = new FileInputStream(in);
        OutputStream output = new FileOutputStream(out);

        if(encrypt) {
            output = new CipherOutputStream(output, cipher);
        } else {
            input = new CipherInputStream(input, cipher);
        }

        while ((numRead = input.read(buffer)) >= 0) {
            output.write(buffer, 0, numRead);
        }

        input.close();
        output.close();
    }
}
