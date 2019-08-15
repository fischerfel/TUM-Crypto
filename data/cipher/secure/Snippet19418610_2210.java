import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.security.Security;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

import de.flexiprovider.core.FlexiCoreProvider;

public class ExampleCrypt123 {

    public static void main(String[] args) throws Exception {
        Security.addProvider(new FlexiCoreProvider());
        Cipher cipher = Cipher.getInstance("AES128_CBC", "FlexiCore");

        KeyGenerator keyGen = KeyGenerator.getInstance("AES", "FlexiCore");
        SecretKey secKey = keyGen.generateKey();

        String cleartextFile = "F:/java_projects/cleartext.txt";
        String ciphertextFile = "F:/java_projects/ciphertextSymm.txt";
        String cleartextAgainFile = "F:/java_projects/cleartextAgainSymm.txt";

        byte[] block = new byte[16];

        FileInputStream fis = new FileInputStream(cleartextFile);
        FileOutputStream fos = new FileOutputStream(ciphertextFile);

        cipher.init(Cipher.ENCRYPT_MODE, secKey);
        // Encrypt
        long startTime = System.nanoTime();

        for(int j=0; j< 1000000; j++){
            CipherOutputStream cos = new CipherOutputStream(fos, cipher);

            int i;
            while ((i = fis.read(block)) != -1) {
                cos.write(block, 0, i);
            }
            cos.close();
        }
        long elapsedTime = System.nanoTime() - startTime;

        System.out.println("Total execution time to create 1000 objects in Java in millis: "
                + elapsedTime/1000000);

        // Decrypt



        fis = new FileInputStream(ciphertextFile);

        fos = new FileOutputStream(cleartextAgainFile);

        cipher.init(Cipher.DECRYPT_MODE, secKey);

        long startTime2 = System.nanoTime();
        for(int j=0; j< 1000000; j++){
            CipherInputStream cis = new CipherInputStream(fis, cipher);
            int i;

            while (( i = cis.read(block)) != -1) {
                fos.write(block, 0, i);
            }
            fos.close();
        }
        long elapsedTime2 = System.nanoTime() - startTime2;

        System.out.println("Total execution time to create 1000  objects in Java in millis: "
                + elapsedTime2/1000000);
    }
