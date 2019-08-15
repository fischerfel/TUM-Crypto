import java.security.SecureRandom;
import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.FileInputStream;
import java.io.FileOutputStream;

public class FileReadWrite {

    public static byte[] getAESKey() {
        SecureRandom secureRandom = new SecureRandom();
        byte[] bytes = new byte[32];
        secureRandom.nextBytes(bytes);
        return bytes;
    }
/**
 * Method used to generate a random new iv
 * 
 * @return Randomly generated iv
 */
public static byte[] getAESIV() {
    SecureRandom secureRandom = new SecureRandom();
    byte[] bytes = new byte[16];
    secureRandom.nextBytes(bytes);
    return bytes;
}
public static void main(String[] args) throws Exception {
    // TODO Auto-generated method stub
    FileInputStream fin = new FileInputStream("/Users/emp/Research/Streams/input_file.txt");
    FileOutputStream fout = new FileOutputStream("/Users/emp/Research/Streams/output_file.txt");

    SecretKeySpec keySpec = null;
    IvParameterSpec ivSpec = null;
    Cipher ecipher = null;
    Cipher dcipher = null;
    byte[] keyBytes = getAESKey();
    byte[] ivBytes = getAESIV();
    // Creating keyspec and ivspec for generating cipher
    keySpec = new SecretKeySpec(keyBytes,"AES");
    ivSpec = new IvParameterSpec(ivBytes);
    try {
        ecipher = Cipher.getInstance("AES/CTR/NoPadding");
        ecipher.init(Cipher.ENCRYPT_MODE, keySpec, ivSpec);
    } catch (Exception e) {
        System.out.println("Thus the exception occured during cipher generation is ::: "+e);
    }

    CipherOutputStream cout = new CipherOutputStream(fout, ecipher);
    try {
        int count = 0;
        int BUFFER_SIZE = 1024;
        byte[] bytearray = new byte[BUFFER_SIZE];
        while((count = fin.read(bytearray, 0, BUFFER_SIZE)) != -1) {
            //fout.write(bytearray, 0, count);
            cout.write(bytearray, 0, count);
        }
    } catch(Exception ex) {
        System.out.println("Thus the exception occured is ::: "+ex);
    } finally {
        fin.close();
        fout.close();
        cout.close();
    }

    fin = new FileInputStream("/Users/emp/Research/Streams/output_file.txt");
    fout = new FileOutputStream("/Users/emp/Research/Streams/decrypted_file.txt");

    try {
        dcipher = Cipher.getInstance("AES/CTR/NoPadding");
        dcipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec);
    } catch (Exception e) {
        System.out.println("Thus the exception occured during cipher generation is ::: "+e);
    }
    //fin.skip(1024);
    CipherInputStream cin = new CipherInputStream(fin, dcipher);
    try {
        int count = 0;
        int BUFFER_SIZE = 1024;
        byte[] bytearray = new byte[BUFFER_SIZE];

        **//cin.read(bytearray, 0, 30);**

        while((count = cin.read(bytearray, 0, BUFFER_SIZE)) != -1) {
            //fout.write(bytearray, 0, count);
            fout.write(bytearray, 0, count);
        }
    } catch(Exception ex) {
        System.out.println("Thus the exception occured is ::: "+ex);
    } finally {
        fin.close();
        cin.close();
        fout.close();
    }

    System.out.println("File read write completed successfully !!! ");
}
}
