import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;


public class Test {

    private static String strkey ="Blowfish";
    private static Base64 base64 = new Base64(true);

     //encrypt using blowfish algorithm
    public static String encrypt(String Data)throws Exception{

        SecretKeySpec key = new SecretKeySpec(strkey.getBytes("UTF8"), "Blowfish");
         Cipher cipher = Cipher.getInstance("Blowfish");
         cipher.init(Cipher.ENCRYPT_MODE, key);

         return base64.encodeToString(cipher.doFinal(Data.getBytes("UTF8")));

    }

    //decrypt using blow fish algorithm
    public static String decrypt(String encrypted)throws Exception{
        byte[] encryptedData = base64.decodeBase64(encrypted);
         SecretKeySpec key = new SecretKeySpec(strkey.getBytes("UTF8"), "Blowfish");
         Cipher cipher = Cipher.getInstance("Blowfish");
         cipher.init(Cipher.DECRYPT_MODE, key);
         byte[] decrypted = cipher.doFinal(encryptedData);
         return new String(decrypted); 

    }

    public static void main(String[] args) throws Exception {
        String data = "will this work?";
        String encoded = encrypt(data);
        System.out.println(encoded);
        String decoded = decrypt(encoded);
        System.out.println(decoded);
    }
}
