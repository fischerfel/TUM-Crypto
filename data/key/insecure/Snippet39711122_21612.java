import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.security.*;
import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

//create public class encrypt
public class encrypt {
    //algorithm AES 128 with a secret key
    private static final String ALGO = "AES";
    private static final byte[] keyValue = 
        new byte[] { 'H', 't', 'v', 'b', 'a', 'w', 'e',
'i', 'n', 'v', 'a','l', 't', 'k', 'y', 'e' };
    private static BufferedReader reader;

    public static void main(String[] args) throws Exception {
        //Filereader letter to read from a file letter.txt
        FileReader letter = new FileReader("/Users/Shiv/Eclipse/CPS3498_HW/src/letter.txt");
        reader = new BufferedReader(letter);
        //string text blank, data that stres reader contents.
        String text = "";
        String data = reader.readLine();
        //while loop to see if data is not blank
        while (data != null){
            text += data;
            data = reader.readLine();
        }
        String textEnc = encrypt(text);
        //        
        File secret = new File("/Users/Shiv/Eclipse/CPS3498_HW/src/secret.txt");
            try
            {
                secret.createNewFile();
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }           
            try {
                FileWriter secretFile = new FileWriter(secret);
                BufferedWriter secretBuff = new BufferedWriter(secretFile);
                secretBuff.write(textEnc);
                secretBuff.close();
            } 
            catch (Exception e) 
            {
                e.printStackTrace();
            }
        }
//encrypt method
public static String encrypt(String Data) throws Exception {
        Key pass = generateKey();
        // cipher class to provide the encryption and intialize
        Cipher c = Cipher.getInstance(ALGO);
        c.init(Cipher.ENCRYPT_MODE, pass);
        byte[] encVal = c.doFinal(Data.getBytes());
        String encryptedValue = Base64.getEncoder().encodeToString(encVal);
        return encryptedValue;     
    }
//generateKey method to generate a secret key
private static Key generateKey() throws Exception {
    Key pass = new SecretKeySpec(keyValue, ALGO);
    return pass;
}
}
