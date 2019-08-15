import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.xml.bind.DatatypeConverter;

public class MainClass {

    public static void main(String[] args) {

        String l = "0e329232ea6d0d73";

        byte[] a = DatatypeConverter.parseHexBinary(l);

        try{
            DESKeySpec dks = new DESKeySpec(a);
            SecretKeyFactory skf = SecretKeyFactory.getInstance("DES");
            SecretKey sk = skf.generateSecret(dks);
        Cipher c = Cipher.getInstance("DES");
        c.init(Cipher.ENCRYPT_MODE, sk);
        String M = "8787878787878787";
        byte[] b = c.doFinal(M.getBytes());

        System.out.println(new String(b));
        c.init(Cipher.DECRYPT_MODE, sk);
        System.out.println(new String(c.doFinal(b)));
        }
        catch(Exception e)

        {
            System.out.println(e.getMessage());
        }   
    }

}
