import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Date;
import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;


public class AES_Cipher_Test {


    public String pLocalRef = "E:\\Test.txt";
    public String pLocalRefOutput = "E:\\Test-crypted.txt";
    public String pLocalCopyOutput = "E:\\Test-Neu.txt";
    public Key pKeyAES = null;
    public int pBitKey = 128;
    public Cipher pCipher;  
    public FileOutputStream pFos;
    public FileInputStream pFis;
    public CipherOutputStream pCos;
    public CipherInputStream pCis;
    public File pInputFile = new File(this.pLocalRef);
    public File pOutputFile = new File(this.pLocalRefOutput);
    public File pGeneratedFile = new File(this.pLocalCopyOutput);

    public AES_Cipher_Test() {
        crypt_decrypt_write_File();
    }

    public void crypt_decrypt_write_File() {
        byte[] lLoadedFile = null;
        byte[] lGeneratedFileByte = null;
        try {

            // generate new random AES Key
            KeyGenerator lKeygen = KeyGenerator.getInstance("AES");
            lKeygen.init(this.pBitKey);
            this.pKeyAES = lKeygen.generateKey();


            // read input File
            this.pFis = new FileInputStream(this.pInputFile);
            FileInputStream tempStream = new FileInputStream(this.pInputFile);
            int count = 0;
            while (tempStream.read() != -1){
                count ++;
            }
            lLoadedFile = new byte[count]; // new byte[this.pFis.available()]
            this.pFis.read(lLoadedFile);
            System.err.println("lLoadedFile.legth  " + lLoadedFile.length);
            this.pFis.close();

            //init Cipher with AES Encrypt Mode CFB8 oder CTR
            this.pCipher = Cipher.getInstance("AES/CTR/PKCS5Padding");
            this.pCipher.init(Cipher.ENCRYPT_MODE, this.pKeyAES);


            // build cipher stream from FileOutputStream
            this.pFos = new FileOutputStream(this.pOutputFile);
            this.pCos = new CipherOutputStream(this.pFos, this.pCipher);

            //write encrypted Data to stream
            this.pCos.write(lLoadedFile);
            this.pCos.close();
            this.pFos.close();

            // init Cipher for decrypt Mode
            this.pCipher.init(Cipher.DECRYPT_MODE, this.pKeyAES, new IvParameterSpec(this.pCipher.getIV()));


            // read just written localFile and decrypt
            this.pFis = new FileInputStream(this.pOutputFile);
            tempStream = new FileInputStream(this.pOutputFile);
            count = 0;
            while (tempStream.read() != -1){
                count ++;
            }
            byte[] lBytes = new byte[count];// new byte[this.pFis.available()]
            this.pCis = new CipherInputStream(this.pFis, this.pCipher);
            int lBytesRead = this.pCis.read(lBytes);
            while (lBytesRead > -1) {
                lBytesRead = this.pCis.read(lBytes);
            }
            this.pCis.close();
            this.pFis.close();
            System.err.println("lBytes.length " + lBytes.length);

            // write new not crypted File to see if procedure works
            this.pFos = new FileOutputStream(this.pLocalCopyOutput);
            this.pFos.write(lBytes);
            this.pFos.close();


            //compare Input File and Output File
            this.pFis = new FileInputStream(this.pGeneratedFile);
            tempStream = new FileInputStream(this.pGeneratedFile);
            count = 0;
            while (tempStream.read() != -1){
                count ++;
            }
            lGeneratedFileByte = new byte[count]; // new byte[this.pFis.available()]
            int i = this.pFis.read(lGeneratedFileByte);
            this.pFis.close();

            System.err.println("lGeneratedFileByte.length " + i);
            System.err.println("Test if initial File and new File are identical = " + Arrays.equals(lGeneratedFileByte, lLoadedFile));


        } catch (FileNotFoundException e) {
            throw new RuntimeException("FILE_DOES_NOT_EXIST", e);
        } catch (NoSuchAlgorithmException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (InvalidAlgorithmParameterException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }


    public static void main(String args[]) {
        System.err.println("Start AES_Cipher_Test");
        long start = new Date().getTime();
        new AES_Cipher_Test();
        long runningTime = new Date().getTime() - start;
        System.err.println("End AES_Cipher_Test");
        System.err.println("Runtime: " + runningTime);

    }
}
