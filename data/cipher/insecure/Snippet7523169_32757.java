import java.security.*;
import java.security.cert.*;
import java.security.interfaces.*;
import java.security.spec.*;
import javax.crypto.*;
import javax.crypto.interfaces.*;
import javax.crypto.spec.*;
import java.io.*;
/**
 *  AES
 */
public class SymmetricAES {

    public SymmetricAES() {
    }

    public static void main(String args[]){
        BufferedReader ch = new BufferedReader(new InputStreamReader(System.in));
        char[] toCode;
        byte[] toCode2;
        byte[] Coded;
        char[] Coded2;
        byte[] decoded;
        char[] deco2;

        try{
            System.out.print("Text to Encrypt : ");
            String toMake = ch.readLine();

            /** Arxikopoihsh antikeimenou KeyGenerator gia AES
             *  kai dhmhourgia Kleidioy */
            KeyGenerator keyGen = KeyGenerator.getInstance("AES");
            SecretKey aesKey = keyGen.generateKey();

            /* Arxikopoihsh aesCipher gia AES */
            Cipher aesCipher = Cipher.getInstance("AES");

            /* Orismos aesCipher se ENCRYPT_MODE me to aesKey */
            aesCipher.init(Cipher.ENCRYPT_MODE, aesKey);
            System.out.println("The Key : " + aesKey.getEncoded());

            /* Metatroph antikeimenou String se pinaka Char
             * kai o ka8e xarakthras gineta Cast se Byte kai eisagwgh
             * se pinaka Byte isou mege8ous me ton prwto pinaka */
            toCode = toMake.toCharArray();
            toCode2 = new byte[toCode.length];
            Coded = new byte[toCode.length];
            for(int i=0;i<toCode.length;i++)
                toCode2[i] = (byte)toCode[i];

            /* Teliko stadio Kryptografhshs */
            Coded = aesCipher.doFinal(toCode2);

            /* byte[] --> char[] kai ektypwsh*/
            Coded2 = new char[Coded.length];
            for(int i=0;i<Coded.length;i++)
                Coded2[i] = (char)Coded[i];
            System.out.println("Test Encrypt: " + new String(Coded2));


            Cipher aesCipher2 = Cipher.getInstance("AES");

            /* Orismos aesCipher2 se DECRYPT_MODE me to aesKey */
            aesCipher2.init(Cipher.DECRYPT_MODE, aesKey);

            decoded = aesCipher2.doFinal(Coded);

            /* byte[] --> char[] kai ektypwsh*/
            deco2 = new char[decoded.length];
            for(int i=0;i<decoded.length;i++)
                deco2[i] = (char)decoded[i];
            System.out.println("Test Decrypt: " + new String(deco2));
        }
        catch(Exception e){
            System.out.println(e);
        }   
    }
}
