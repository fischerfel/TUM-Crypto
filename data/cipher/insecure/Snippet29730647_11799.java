import de.flexiprovider.api.keys.SecretKey;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import de.flexiprovider.core.FlexiCoreProvider;
import de.flexiprovider.core.rc5.RC5KeyGenerator;
import de.flexiprovider.core.rc5.RC5ParameterSpec;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Security;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;

public class RC5moje {

    private int roundNumer;
    private int wordSize;

    private SecretKey key;
    private RC5ParameterSpec RC5params;
    private Cipher rc5;

   public RC5moje(int roundNumer, int wordSize) {
        this.roundNumer = roundNumer;
        this.wordSize = wordSize;

        Security.addProvider(new FlexiCoreProvider());
        this.RC5params = new RC5ParameterSpec(roundNumer, wordSize);
        try {
            this.rc5 = Cipher.getInstance("RC5", "FlexiCore");
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(RC5moje.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NoSuchProviderException ex) {
            Logger.getLogger(RC5moje.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NoSuchPaddingException ex) {
            Logger.getLogger(RC5moje.class.getName()).log(Level.SEVERE, null, ex);
        }
        RC5KeyGenerator rC5KeyGenerator = new RC5KeyGenerator();
        this.key = rC5KeyGenerator.generateKey();

    }

    public void encrypt(String inFile, String outFile) {
        try {

            rc5.init(Cipher.ENCRYPT_MODE, key, RC5params);

            FileInputStream fis = new FileInputStream(inFile);
            FileOutputStream fos = new FileOutputStream(outFile);
            CipherOutputStream cos = new CipherOutputStream(fos, rc5);

            byte[] block = new byte[8];
            int i;
            while ((i = fis.read(block)) != -1) {
                cos.write(block, 0, i);
            }
            cos.close();

        } catch (InvalidKeyException ex) {
            Logger.getLogger(RC5moje.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvalidAlgorithmParameterException ex) {
            System.out.println(ex.getMessage());
            Logger.getLogger(RC5moje.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(RC5moje.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void decrypt(String inFile, String outFile) {

        try {
            rc5.init(Cipher.DECRYPT_MODE, key,RC5params);

            FileInputStream fis;

            fis = new FileInputStream(inFile);

            FileOutputStream fos = new FileOutputStream(outFile);
            CipherInputStream cis = new CipherInputStream(fis, rc5);

            byte[] block = new byte[8];
            int i;

            while ((i = cis.read(block)) != -1) {
                fos.write(block, 0, i);
            }
            fos.close();
        } catch (InvalidKeyException ex) {
            Logger.getLogger(RC5moje.class.getName()).log(Level.SEVERE, null, ex);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(RC5moje.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(RC5moje.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
