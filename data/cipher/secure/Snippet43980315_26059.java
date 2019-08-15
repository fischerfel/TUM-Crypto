package test1;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.AlgorithmParameterSpec;
import java.util.Scanner;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class Encrypter {
    private final static int IV_LENGTH = 16;
    private final static String ALGO_RANDOM_NUM_GENERATOR = "SHA1PRNG";
    private final static String ALGO_SECRET_KEY_GENERATOR = "AES";
    private final static String ALGO_VIDEO_ENCRYPTOR = "AES/CTR/NoPadding"; //Just a string

    static String videoPath = "";

  //-------------------------------------------------------------------------------------------------------------------------------------------------

    @SuppressWarnings("resource")
    public static void encrypt(SecretKey key, AlgorithmParameterSpec paramSpec, InputStream in, OutputStream out)
            throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException,
            InvalidAlgorithmParameterException, IOException, Exception {
                Cipher c = Cipher.getInstance(ALGO_VIDEO_ENCRYPTOR);
                c.init(Cipher.ENCRYPT_MODE, key, paramSpec);
                EncryptTask task1 = new EncryptTask(key, paramSpec, in, out, c);
                EncryptTask task2 = new EncryptTask(key, paramSpec, in, out, c);
                EncryptTask task3 = new EncryptTask(key, paramSpec, in, out, c);
                EncryptTask task4 = new EncryptTask(key, paramSpec, in, out, c);
                Thread t1 = new Thread(task1);
                Thread t2 = new Thread(task2);
                Thread t3 = new Thread(task3);
                Thread t4 = new Thread(task4);
                t1.start();
                t2.start();
                t3.start();
                t4.start();
                t1.join();
                t2.join();
                t3.join();
                t4.join();
    }

//-------------------------------------------------------------------------------------------------------------------------------------------------

    @SuppressWarnings("resource")
    public static void decrypt(SecretKey key, AlgorithmParameterSpec paramSpec, InputStream in, OutputStream out)
            throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException,
            InvalidAlgorithmParameterException, IOException, Exception {
                Cipher c = Cipher.getInstance(ALGO_VIDEO_ENCRYPTOR);
                c.init(Cipher.DECRYPT_MODE, key, paramSpec);
                DecryptTask task1 = new DecryptTask(key, paramSpec, in, out, c);
                DecryptTask task2 = new DecryptTask(key, paramSpec, in, out, c);
                DecryptTask task3 = new DecryptTask(key, paramSpec, in, out, c);
                DecryptTask task4 = new DecryptTask(key, paramSpec, in, out, c);
                Thread t1 = new Thread(task1);
                Thread t2 = new Thread(task2);
                Thread t3 = new Thread(task3);
                Thread t4 = new Thread(task4);
                t1.start();
                t2.start();
                t3.start();
                t4.start();
                t1.join();
                t2.join();
                t3.join();
                t4.join();
    }

  //-------------------------------------------------------------------------------------------------------------------------------------------------

    @SuppressWarnings("resource")
    public static void main(String[] args) throws Exception { //Main class

        System.out.print("Enter file's path(Example D:/test/copy.mp4): ");
        Scanner scanVideo = new Scanner(System.in); 
        videoPath = scanVideo.nextLine();

        File inFile = new File(videoPath);
        File outFile = new File("D:/test/enc_video.mp4");
        File outFile_dec = new File("D:/test/dec_video.mp4");

        long tStart = System.currentTimeMillis();

        try {
            SecretKey key = KeyGenerator.getInstance(ALGO_SECRET_KEY_GENERATOR).generateKey();

            byte[] keyData = key.getEncoded();
            SecretKey key2 = new SecretKeySpec(keyData, 0, keyData.length, ALGO_SECRET_KEY_GENERATOR);
            byte[] iv = new byte[IV_LENGTH];

            SecureRandom.getInstance(ALGO_RANDOM_NUM_GENERATOR).nextBytes(iv);

            AlgorithmParameterSpec paramSpec = new IvParameterSpec(iv);

            Encrypter.encrypt(key, paramSpec, new FileInputStream(inFile), new FileOutputStream(outFile));
            Encrypter.decrypt(key2, paramSpec, new FileInputStream(outFile), new FileOutputStream(outFile_dec));
        } catch (Exception e) {
            e.printStackTrace();
        }

        long tEnd = System.currentTimeMillis();
        long tDelta = tEnd - tStart;
        double elapsedSeconds = tDelta / 1000.0;
        System.out.println("Time is: " + elapsedSeconds + " Seconds");

    }

}
