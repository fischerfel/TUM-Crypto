import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Security;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;


public class ExampleRSA {
    private static String VIDEO_SOURCE_FILE = "C:/Users/ggoldman/Desktop/Video/inputVideo.dv";
    private static String EncryptedFile = "C:/Users/ggoldman/Desktop/Video/encVideo.dv";
    private static File decfile = new File("C:/Users/ggoldman/Desktop/Video/decVideo.dv");
    private static File incfile = new File(EncryptedFile);
    private static File sourceMedia = new File(VIDEO_SOURCE_FILE);


    public static void main(String[] args) throws Exception {


        KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
        Cipher cipher = Cipher.getInstance("RSA");

        kpg.initialize(1024);
        KeyPair keyPair = kpg.generateKeyPair();
        PrivateKey privKey = keyPair.getPrivate();
        PublicKey pubKey = keyPair.getPublic();

        // Encrypt

        cipher.init(Cipher.ENCRYPT_MODE, pubKey);


        FileInputStream fis = new FileInputStream(sourceMedia);
        FileOutputStream fos = new FileOutputStream(incfile);
        CipherOutputStream cos = new CipherOutputStream(fos, cipher);

        byte[] block = new byte[32];
        int i;
        while ((i = fis.read(block)) != -1) {
            cos.write(block, 0, i);
        }
        cos.close();
}
