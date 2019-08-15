import java.io.*;
import javax.crypto.*;
import javax.crypto.spec.*;

public class Encrypt {

    public static void main(String[] args) {

        try {
            SecretKey key = KeyGenerator.getInstance("DES").generateKey();

            FileOutputStream fosKey = new FileOutputStream("..\\KEY");
            SecretKeyFactory keyfac = SecretKeyFactory.getInstance("DES");
            DESKeySpec keyspec = (DESKeySpec) keyfac.getKeySpec(key, DESKeySpec.class);
            fosKey.write(keyspec.getKey());
            fosKey.close();

            Cipher crypt = Cipher.getInstance("DES");
            crypt.init(Cipher.ENCRYPT_MODE, key);

            FileInputStream fis = new FileInputStream("..\\File.txt");
            FileOutputStream fos = new FileOutputStream("..\\FileCrypted.txt");
            byte[] arrayBytes = new byte[8];
            int bytesReads;
            while ((bytesReads = fis.read(arrayBytes)) != -1) {
                fos.write(crypt.doFinal(arrayBytes), 0, bytesReads);
            }
            fis.close();
            fos.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
