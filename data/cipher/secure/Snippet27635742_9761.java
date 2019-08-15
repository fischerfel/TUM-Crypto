import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.security.Key;
import java.security.KeyStore;
import java.security.Security;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

import de.flexiprovider.core.FlexiCoreProvider;

public class Decrypto {

    public static void main(String[] args) throws Exception {
        Security.addProvider(new FlexiCoreProvider());

        /*
         * Cipher cipher1 = Cipher.getInstance("AES128_CBC", "FlexiCore");
         * KeyGenerator keyGen = KeyGenerator.getInstance("AES", "FlexiCore");
         * SecretKey secKey = keyGen.generateKey();
         * System.out.println(secKey);
         */
        Cipher cipher1 = Cipher.getInstance("AES128_CBC", "FlexiCore");
        KeyStore keyStore = KeyStore.getInstance("JCEKS");

        FileInputStream fis = new FileInputStream("C:\\mykey.keystore"); // here
                                                                         // i am
                                                                         // uploading
        keyStore.load(fis, "javaci123".toCharArray());
        fis.close();
        Key secKey = (Key) keyStore.getKey("mySecretKey",
                "javaci123".toCharArray()); // line 35

        System.out.println("Found Key: " + (secKey));

        String cleartextFile = "C:\\cleartext.txt";
        String ciphertextFile = "C:\\ciphertextSymm.txt";

        // FileInputStream fis = new FileInputStream(cleartextFile);
        FileOutputStream fos = new FileOutputStream(ciphertextFile);

        String cleartextAgainFile = "C:\\cleartextAgainSymm.txt";

        cipher1.init(Cipher.DECRYPT_MODE, secKey);
        fis = new FileInputStream(ciphertextFile);

        // fis = new FileInputStream(ciphertextFile);
        CipherInputStream cis = new CipherInputStream(fis, cipher1);
        fos = new FileOutputStream(cleartextAgainFile);
        byte[] block = new byte[8];
        int i;
        while ((i = fis.read(block)) != -1) {
            cis.read(block, 0, i);
        }
        cis.close();
    }

}
