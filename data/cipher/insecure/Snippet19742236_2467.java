package cmdCrypto;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;
import javax.xml.bind.DatatypeConverter;

public class CmdCrypto {

    public static void main(String[] args) {
        try{
            final String strPassPhrase = "123456789012345678901234"; //min 24 chars

            String param = "No body can see me";
        System.out.println("Text : " + param);

            SecretKeyFactory factory = SecretKeyFactory.getInstance("DESede");
            SecretKey key = factory.generateSecret(new DESedeKeySpec(strPassPhrase.getBytes()));
            Cipher cipher = Cipher.getInstance("DESede");

            cipher.init(Cipher.ENCRYPT_MODE, key);
            String str = DatatypeConverter.printBase64Binary(cipher.doFinal(param.getBytes()));
        System.out.println("Text Encryted : " + str);

            cipher.init(Cipher.DECRYPT_MODE, key);
        String str2 = new String(cipher.doFinal(DatatypeConverter.parseBase64Binary(str)));
        System.out.println("Text Decryted : " + str2);

        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}
