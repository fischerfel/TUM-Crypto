package Firstage;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.security.SecureRandom;
import java.security.spec.KeySpec;
import java.util.Random;
import java.util.Scanner;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

public class Thealgorithm1 
{

    static Scanner get = new Scanner(System.in);
    private static final String ALGORITHM = "AES";
    private static final String ALGORITHM_MODE ="AES/CFB/NoPadding";
    private static String password;

    public static void encrypt(File inputFile, File outputFile)
            throws Exception
    {
        System.out.println("Enetr passprhase");
        password=get.nextLine();

        final Random ivspc = new SecureRandom();
        byte[] ivspec = new byte[16];
        ivspc.nextBytes(ivspec);
        IvParameterSpec enciv = new IvParameterSpec(ivspec);

        FileOutputStream outputstrm = new FileOutputStream(outputFile);
        byte[] outputBytes = doCrypto(Cipher.ENCRYPT_MODE, inputFile,password,enciv);
        System.arraycopy(ivspec, 0,outputBytes , 0, 16);
        outputstrm.write(outputBytes);
        outputstrm.close();
        System.out.println("File encrypted successfully!");
    }

    public static void decrypt(File inputFile, File outputFile)
            throws Exception
    {

        System.out.println("Enter password");
        password=get.nextLine();
        IvParameterSpec hj = null;
        byte[]outpytBytes=doCrypto(Cipher.DECRYPT_MODE, inputFile,password,hj);
        FileOutputStream outputstrm = new FileOutputStream(outputFile);
        outputstrm.write(outpytBytes);
        outputstrm.close();
        System.out.println("File decrypted successfully!");
    }

    private static byte[] doCrypto(int cipherMode, File inputFile,String keyo ,IvParameterSpec ivespec)
            throws Exception {
    /* Derive the key, given password and salt. */
        final Random slt = new SecureRandom();
        byte[] salt = new byte[8];
        slt.nextBytes(salt);


        char[] passkeyo = keyo.toCharArray();
        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
        KeySpec spec = new PBEKeySpec(passkeyo, salt, 65536, 128);
        SecretKey tmp = factory.generateSecret(spec);

        SecretKey secret = new SecretKeySpec(tmp.getEncoded(), ALGORITHM);

        FileInputStream fylin = new FileInputStream(inputFile);
        byte[] inputBytes = new byte[(int)inputFile.length()];
        fylin.read(inputBytes);
        fylin.close();

        Cipher cipher = Cipher.getInstance(ALGORITHM_MODE);

        byte[] outputBytes;
        if(cipherMode==2)
        {
            IvParameterSpec ivdec = new IvParameterSpec(inputBytes,0,16);
            cipher.init(cipherMode, secret,ivdec);
        }
        else
        {
            cipher.init(cipherMode, secret, ivespec);
        }


        if(cipherMode==2)
        {
            outputBytes = cipher.doFinal(inputBytes, 16,(inputBytes.length-16));
        }
        else
        {
            outputBytes=cipher.doFinal(inputBytes);
        }


        return outputBytes;

    }

    public static void main(String[] args)
    {

        File inputFile = new File("C:/temp/File.txt");
        File encryptedFile = new File("C:/temp/encryaes.enc");
        File decryptedFile = new File("C:/temp/mydr.txt");


        try {

            Thealgorithm1.encrypt(inputFile, encryptedFile);
            Thealgorithm1.decrypt(encryptedFile, decryptedFile);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

    }

}
