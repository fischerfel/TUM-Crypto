import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;


public class Crypto {

    public FileInputStream mIn;
    public FileOutputStream mOut;
    public Crypto(String fileIn, String fileOut) {
        try {
                mIn = new FileInputStream(new File(fileIn));
                mOut = new FileOutputStream(new File(fileOut));
                decryptAES(mIn, mOut);
        } catch (Exception e) {
                e.printStackTrace();
        }
    }

    public static void decryptAES(InputStream in, FileOutputStream out) throws Exception {
        final String string = "346a23652a46392b4d73257c67317e352e3372482177652c";
        byte[] hexAsBytes = DatatypeConverter.parseHexBinary(string);

        SecretKeySpec keySpec = new SecretKeySpec(hexAsBytes, "AES");
        Cipher cipher = Cipher.getInstance("AES"); 

        cipher.init(Cipher.DECRYPT_MODE, keySpec);

        in = new CipherInputStream(in, cipher);
        byte[] buffer = new byte[24]; 
        int bytesRead;
        while ((bytesRead = in.read(buffer)) != -1)
        {
            out.write(buffer, 0, bytesRead);
        }

    }


    public static void main(String[] args){    
        Crypto c = new Crypto("C:\\msgstore.db.crypt", "D:\\WhatsappDb");
        System.out.println("Decrypting Done");
    }
}
