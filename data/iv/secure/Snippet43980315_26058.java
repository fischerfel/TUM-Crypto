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
import javax.crypto.CipherOutputStream;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class EncrypterSeq {
    private final static int IV_LENGTH = 16; 
    private final static int DEFAULT_READ_WRITE_BLOCK_BUFFER_SIZE = 1024;

    private final static String ALGO_RANDOM_NUM_GENERATOR = "SHA1PRNG";
    private final static String ALGO_SECRET_KEY_GENERATOR = "AES";
    private final static String ALGO_VIDEO_ENCRYPTOR = "AES/CTR/PKCS5Padding";
    static String videoPath = "";
  //-------------------------------------------------------------------------------------------------------------------------------------------------
    @SuppressWarnings("resource")
    public static void encrypt(SecretKey key, AlgorithmParameterSpec paramSpec, InputStream in, OutputStream out)
            throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException,
            InvalidAlgorithmParameterException, IOException {
        try {
            Cipher c = Cipher.getInstance(ALGO_VIDEO_ENCRYPTOR);
            c.init(Cipher.ENCRYPT_MODE, key, paramSpec);
            out = new CipherOutputStream(out, c);

            int count = 0;
            byte[] buffer = new byte[DEFAULT_READ_WRITE_BLOCK_BUFFER_SIZE];
            while ((count = in.read(buffer)) >= 0) {
                out.write(buffer, 0, count);
            }
        } finally {
            out.close();
        }
    }
  //-------------------------------------------------------------------------------------------------------------------------------------------------
    @SuppressWarnings("resource")
    public static void decrypt(SecretKey key, AlgorithmParameterSpec paramSpec, InputStream in, OutputStream out)
            throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException,
            InvalidAlgorithmParameterException, IOException {
        try {
            Cipher c = Cipher.getInstance(ALGO_VIDEO_ENCRYPTOR);
            c.init(Cipher.DECRYPT_MODE, key, paramSpec);
            out = new CipherOutputStream(out, c);

            int count = 0;
            byte[] buffer = new byte[DEFAULT_READ_WRITE_BLOCK_BUFFER_SIZE];
            while ((count = in.read(buffer)) >= 0) {
                out.write(buffer, 0, count);
            }
        } finally {
            out.close();
        }
    }
  //-------------------------------------------------------------------------------------------------------------------------------------------------
    public static void main(String[] args) {

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

            EncrypterSeq.encrypt(key, paramSpec, new FileInputStream(inFile), new FileOutputStream(outFile));
            EncrypterSeq.decrypt(key2, paramSpec, new FileInputStream(outFile), new FileOutputStream(outFile_dec));
        } catch (Exception e) {
            e.printStackTrace();
        }
        long tEnd = System.currentTimeMillis();
        long tDelta = tEnd - tStart;
        double elapsedSeconds = tDelta / 1000.0;
        System.out.println("Time is: " + elapsedSeconds + " Seconds");

    }

}
