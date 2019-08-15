import java.io.*;
import javax.crypto.*;
import javax.crypto.spec.*;

public class Decrypt {

    public static void main(String[] args) {

        try {
            FileInputStream fisKey = new FileInputStream("..\\KEY");
            byte[] arrayKey = new byte[fisKey.available()];
            fisKey.read(arrayKey);
            SecretKey key = new SecretKeySpec(arrayKey, "DES");

            Cipher decrypt = Cipher.getInstance("DES");
            decrypt.init(Cipher.DECRYPT_MODE, key);

            FileInputStream fis = new FileInputStream("..\\FileCrypted.txt");
            byte[] encText = new byte[16];
            int bytesReads;
            while ((bytesReads = fis.read(encText)) != -1) {
                fis.read(decrypt.doFinal(encText), 0, bytesReads);
            }
            fis.close();
            System.out.println(new String(encText));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
