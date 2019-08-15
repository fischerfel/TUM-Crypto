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
import javax.crypto.Cipher;
import javax.crypto.CipherOutputStream;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
public class Encrypter {

        private final static int IV_LENGTH = 16; // Default length with Default 128
                                                // key AES encryption
        private final static int DEFAULT_READ_WRITE_BLOCK_BUFFER_SIZE = 1024;

        private final static String ALGO_RANDOM_NUM_GENERATOR = "SHA1PRNG";
        private final static String ALGO_SECRET_KEY_GENERATOR = "AES";
        private final static String ALGO_VIDEO_ENCRYPTOR = "AES/CBC/PKCS5Padding";

        @SuppressWarnings("resource")
        public static void encrypt(SecretKey key, AlgorithmParameterSpec paramSpec, InputStream in, OutputStream out)
            throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException,
            InvalidAlgorithmParameterException, IOException {
        try {
            // byte[] iv = new byte[] { (byte) 0x8E, 0x12, 0x39, (byte) 0x9C,
            // 0x07, 0x72, 0x6F, 0x5A, (byte) 0x8E, 0x12, 0x39, (byte) 0x9C,
            // 0x07, 0x72, 0x6F, 0x5A };
            // AlgorithmParameterSpec paramSpec = new IvParameterSpec(iv);
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

    @SuppressWarnings("resource")
    public static void decrypt(SecretKey key, AlgorithmParameterSpec paramSpec, InputStream in, OutputStream out)
            throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException,
            InvalidAlgorithmParameterException, IOException {
        try {
            // byte[] iv = new byte[] { (byte) 0x8E, 0x12, 0x39, (byte) 0x9C,
            // 0x07, 0x72, 0x6F, 0x5A, (byte) 0x8E, 0x12, 0x39, (byte) 0x9C,
            // 0x07, 0x72, 0x6F, 0x5A };
            // AlgorithmParameterSpec paramSpec = new IvParameterSpec(iv);
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

    public static void main(String[] args) {
        File inFile = new File("C:/video_output/VID-20130728-WA0000.avi");
        File outFile = new File("C:/video_output/enc_video.avi");
        File outFile_dec = new File("C:/video_output/dec_video.avi");

        try {
            SecretKey key = KeyGenerator.getInstance(ALGO_SECRET_KEY_GENERATOR).generateKey();

            byte[] keyData = key.getEncoded();
            SecretKey key2 = new SecretKeySpec(keyData, 0, keyData.length, ALGO_SECRET_KEY_GENERATOR); //if you want to store key bytes to db so its just how to //recreate back key from bytes array

            byte[] iv = new byte[IV_LENGTH];
            SecureRandom.getInstance(ALGO_RANDOM_NUM_GENERATOR).nextBytes(iv); // If
                                                                                // storing
                                                                                // separately
            AlgorithmParameterSpec paramSpec = new IvParameterSpec(iv);

            Encrypter.encrypt(key, paramSpec, new FileInputStream(inFile), new FileOutputStream(outFile));
            Encrypter.decrypt(key2, paramSpec, new FileInputStream(outFile), new FileOutputStream(outFile_dec));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
