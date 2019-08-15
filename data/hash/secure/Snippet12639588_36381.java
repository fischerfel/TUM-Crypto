import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.Provider;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;

import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

public class myClassName123 {

/**
 * @param args
 * @throws Exception 
 */
public static void main(String[] args) throws Exception {
    // TODO Auto-generated method stub

    try {
        BufferedReader in = new BufferedReader(new FileReader("/Users/username/Documents/f12/words.txt"));
        String str;
        while ((str = in.readLine()) != null && str.getBytes().length<16) { //clue given in spec

                System.out.println(decrypt(getBytesFromFile(new File("/Users/username/Documents/f21/some.aes-128-cbc")),str));


        }
        in.close();
    } catch (IOException e) {
    }
}

/**
* This method decrypts the input byte [] using AES Key byte [] 
* 
* @param byte []
* @param byte [] 
* @return byte []
* @throws Exception
*/
public static byte[] decrypt(byte[] text, String key) throws Exception {
    Cipher cipher;
    byte[] bytes = null;


    Provider provider = new BouncyCastleProvider();
    MessageDigest digester = MessageDigest.getInstance("SHA-256", provider);
    digester.update(key.getBytes("UTF-8"));
    //byte[] key = digester.digest();
    SecretKeySpec spec = new SecretKeySpec(digester.digest(), "AES");

    //SecretKeySpec spec = new SecretKeySpec(toByteArray(key.toCharArray()), "AES");
     byte[] iv = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
        IvParameterSpec ivspec = new IvParameterSpec(iv);
    try {
            // Instantiate the cipher
            cipher = Cipher.getInstance("AES/CBC/NoPadding");
            cipher.init(Cipher.DECRYPT_MODE, spec, ivspec);

             bytes = cipher.doFinal(text);

             String value = new String(bytes, "UTF-8");

                System.out.println("DEBUG HERE: "+value);

    }catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        //  throw new Exception(e);
    } catch (NoSuchPaddingException e) {
        e.printStackTrace();
    //  throw new Exception(e);
    } catch (InvalidKeyException e) {
        e.printStackTrace();
        //throw new Exception(e);
    } catch (IllegalBlockSizeException e) {
        e.printStackTrace();
        //throw new Exception(e);
    } catch (BadPaddingException e) {
        e.printStackTrace();
        //throw new Exception(e);
    }
    return bytes;
}

// Returns the contents of the file in a byte array.
public static byte[] getBytesFromFile(File file) throws IOException {
    InputStream is = new FileInputStream(file);

    // Get the size of the file
    long length = file.length();

    // Create the byte array to hold the data
    byte[] bytes = new byte[(int)length];

    // Read in the bytes
    int offset = 0;
    int numRead = 0;
    while (offset < bytes.length
           && (numRead=is.read(bytes, offset, bytes.length-offset)) >= 0) {
        offset += numRead;
    }

    // Ensure all the bytes have been read in
    if (offset < bytes.length) {
        throw new IOException("Could not completely read file "+file.getName());
    }

    // Close the input stream and return bytes
    is.close();
    return bytes;
}   
}
