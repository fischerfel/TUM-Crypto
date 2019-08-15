package encryption;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.Security;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

public class Decrypter {

    private static final String PASSWORD = "t_9Y#i@eT[h3}-7!";
    private static final String KEY_ALGORITHM = "PBEWithMD5AndDES";
    private static final String CIPHER_ALGORITHM = "RC4"; //Using Salsa20 or HC256 solves the problem
    private static final String PROVIDER = "BC";

    public static void main(String[] args) throws Exception {
        Security.addProvider(new BouncyCastleProvider());

        File inputFile = new File(args[0]);
        File outputFile = new File(args[1]);

        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(KEY_ALGORITHM);
        SecretKey key = keyFactory.generateSecret(new PBEKeySpec(PASSWORD.toCharArray()));

        Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, key);

        InputStream inputStream = new FileInputStream(inputFile);
        OutputStream outputStream = new FileOutputStream(outputFile);

        CipherInputStream cipherInputStream = new CipherInputStream(inputStream, cipher);

        byte []byteBuffer = new byte[(int)inputFile.length()]; 
        cipherInputStream.read(byteBuffer);
        outputStream.write(byteBuffer); //Only 512bytes of decrypted data is written to file, the rest becomes null
        outputStream.close();
    }

}
