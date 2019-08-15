import java.io.*;
import java.util.*;
import java.security.*;
import javax.crypto.*;
import javax.crypto.spec.*;

public class decryptFile
{
private static SecretKeySpec sec_key_spec = null;
private static Cipher sec_cipher = null;
private static final int SHA1_SIZE = 20;


//decryption function
public static byte[] decrypt(byte[] ciphertext) throws Exception{
    byte[] decrypted = null;
    byte[] iv = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
    IvParameterSpec ivspec = new IvParameterSpec(iv);
    try{
        //set cipher to decrypt mode
        sec_cipher.init(Cipher.DECRYPT_MODE, sec_key_spec, ivspec);

        //do decryption
        decrypted = sec_cipher.doFinal(ciphertext);
    }
    catch (BadPaddingException b)
    {
        sec_cipher.init(Cipher.ENCRYPT_MODE, sec_key_spec, ivspec);
        decrypted = sec_cipher.doFinal(ciphertext);
    }
    catch(Exception e){
        System.out.println(e);
    }

    return decrypted;
}


//creates digest
public static byte[] createDigest(byte[] message) throws Exception
{
    byte[] hash = null;
    try{
        //create message digest object
        MessageDigest sha1 = MessageDigest.getInstance("SHA1");

        //make message digest
        hash = sha1.digest(message);    
    }
    catch(NoSuchAlgorithmException nsae) {
        System.out.println(nsae);
    }
    return hash;
}


//compares two digests
public static String compareDigests(byte[] digest, byte[] local_digest)
{
    for (int i = 0; i < SHA1_SIZE; i++)
    {
        if (digest[i] != local_digest[i]) {
            return "Digests don't match; your file may have been tampered with.";
        }
    }
    return "Digests match!";
}


public static void main (String args[]) 
{
    FileInputStream cFile;
    FileOutputStream mFile;
    byte[] key;
    byte[] key128;
    byte[] ciphertext;
    byte[] decrypted;
    byte[] message;
    byte[] digest;
    byte[] local_digest;
    byte[] local_digest2;
    String mFilename;
    int file_size;

    try {
        //obtains filenames
        String[] cFilename = args[0].split("\\.(?=[^\\.]+$)");
        mFilename = cFilename[0] + "DE." + cFilename[1];
        cFile = new FileInputStream(cFilename[0] + "." + cFilename[1]);

        //reads ciphertext file
        cFile = new FileInputStream(cFilename[0] + "." + cFilename[1]);
        ciphertext = new byte[cFile.available()];
        cFile.read(ciphertext);
        cFile.close();

        //generates key
        key = createDigest(args[1].getBytes("us-ascii"));
        key128 = Arrays.copyOfRange(key, 0, 16);
        sec_key_spec = new SecretKeySpec(key128, "AES");

        //deciphers the plaintext
        sec_cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        decrypted = decrypt(ciphertext);

        //splits plaintext between message and digest
        file_size = decrypted.length;
        message = Arrays.copyOfRange(decrypted, 0, (file_size - SHA1_SIZE));
        digest = Arrays.copyOfRange(decrypted, (file_size - SHA1_SIZE), file_size);

        //compares digests
        local_digest = createDigest(message);
        System.out.println(compareDigests(digest, local_digest));

        //copies message to file
        mFile = new FileOutputStream(mFilename);
        mFile.write(message);
        mFile.close();
    }
    catch (Exception e) {
        System.out.println(e);
    }
 }
}
