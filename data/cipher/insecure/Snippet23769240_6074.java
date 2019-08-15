import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

import android.content.Context;

public class AES extends Cripto {

public static void encrypt(Context c, String nomeArquivo)
        throws IOException, NoSuchAlgorithmException,
        NoSuchPaddingException, InvalidKeyException {



    String key = GenerateKey(); //key 32 character randomly generated


    String pathFilePure = new _Path().getPathFilePure();
    String pathFileCripted = new _Path().getPathFileCripto();

    FileInputStream fis = new FileInputStream(pathFilePure + "/"
            + nomeArquivo);
    FileOutputStream fos = new FileOutputStream(pathFileCripted + "/"
            + nomeArquivo);


    SecretKeySpec sks = new SecretKeySpec(key.getBytes(), "AES");

    Cipher cipher = Cipher.getInstance("AES");
    cipher.init(Cipher.ENCRYPT_MODE, sks);

    CipherOutputStream cos = new CipherOutputStream(fos, cipher);

    int b;
    byte[] d = new byte[8];

    while ((b = fis.read(d)) != -1) {
        cos.write(d, 0, b);
    }

    cos.flush();
    cos.close();
    fis.close();        
}

public static void decrypt(Context c, String fileName, String key)
        throws IOException, NoSuchAlgorithmException,
        NoSuchPaddingException, InvalidKeyException {
    FileInputStream fis = new FileInputStream(
            new _Path().getPathFileDownload() + "/" + fileName);

    FileOutputStream fos = new FileOutputStream(
            new _Path().getPathFileDescripto() + "/" + fileName);
    SecretKeySpec sks = new SecretKeySpec(key.getBytes(), "AES");
    Cipher cipher = Cipher.getInstance("AES");
    cipher.init(Cipher.DECRYPT_MODE, sks);
    CipherInputStream cis = new CipherInputStream(fis, cipher);
    int b;
    byte[] d = new byte[8];

    while ((b = cis.read(d)) != -1) {
        fos.write(d, 0, b);
    }

    fos.flush();
    fos.close();
    cis.close();
}

}
