import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.Cipher;
import javax.crypto.CipherOutputStream;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;

public class FileEncryption {

    private static String PASSWORD = "fake-password-for-stack-overflow-post";
    private static String SOURCE_PATH = "";
    private static String SOURCE_FILE;
    private static String ENCRYPTED_FILE;
    private static String DECRYPTED_FILE;

    private static String ALGORITHM = "PBEWithHmacSHA512AndAES_128";
    private static int SALT_SIZE = 8;
    private static int PBEPARAMETERSPEC_ITERATION_COUNT = 100;

    public static void main(String[] args) throws Exception {
        if(args.length != 1) {
            System.out.println("Only accept a single variable, the path to input.txt.");
            System.out.println("(output will go to same dir)");
            return;
        }

        SOURCE_PATH = args[0];
        System.out.println("Set path: " + SOURCE_PATH);

        if( SOURCE_PATH.charAt(SOURCE_PATH.length()-1) != '/' ) {
            SOURCE_PATH += '/';
        }

        SOURCE_FILE = SOURCE_PATH + "plainfile.txt";
        ENCRYPTED_FILE = SOURCE_PATH + "plainfile.encrypted.txt";
        DECRYPTED_FILE = SOURCE_PATH + "plainfile.decrypted.txt";
        encryptContent(SOURCE_FILE, ENCRYPTED_FILE, PASSWORD);
        decryptContent(ENCRYPTED_FILE, DECRYPTED_FILE, PASSWORD);
    }

    private static void encryptContent(String inputFile, String outputFile, String password) throws Exception {
        Cipher cipher = getCipher(Cipher.ENCRYPT_MODE, password);
        performReadWrite(inputFile, outputFile, cipher);
    }

    private static void decryptContent(String inputFile, String outputFile, String password) throws Exception {
        Cipher cipher = getCipher(Cipher.DECRYPT_MODE, password);
        performReadWrite(inputFile, outputFile, cipher);
        performRead(inputFile, cipher);
    }

    private static void performReadWrite(String inputFile, String outputFile, Cipher cipher)
            throws FileNotFoundException, IOException {

            FileInputStream inFile = new FileInputStream(inputFile);
            FileOutputStream outFile = new FileOutputStream(outputFile);

        CipherOutputStream cos = new CipherOutputStream(outFile, cipher);

        int c;
        while ((c = inFile.read()) != -1)
        {
            cos.write(c);
        }

        cos.close();
        cos = null;
        inFile.close();
        inFile = null;
    }

    private static void performRead(String inputFile, Cipher cipher)
            throws FileNotFoundException, IOException {

            FileInputStream inFile = new FileInputStream(inputFile);

            ByteArrayOutputStream os = new ByteArrayOutputStream();
        CipherOutputStream cos = new CipherOutputStream(os, cipher);

        int c;
        while ((c = inFile.read()) != -1)
        {
            cos.write(c);
        }

        cos.close();
        cos = null;
        inFile.close();
        inFile = null;

//      aClass.outputStreamMethod(os);
            String aString = new String(os.toByteArray(),"UTF-8");
        System.out.println(aString);
    }

    private static Cipher getCipher(int mode, String password) throws NoSuchAlgorithmException, InvalidKeySpecException,
            NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException {

            // Create secret key using password
            PBEKeySpec pbeKeySpec = new PBEKeySpec(password.toCharArray());
            SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance(ALGORITHM);
            SecretKey secretKey = secretKeyFactory.generateSecret(pbeKeySpec);

            // Create the cipher
            byte[] salt = new byte[SALT_SIZE];
            salt = password.getBytes();

            PBEParameterSpec pbeParameterSpec = new PBEParameterSpec(salt, PBEPARAMETERSPEC_ITERATION_COUNT);
            Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(mode, secretKey, cipher.getParameters());
            return cipher;
    }

}
