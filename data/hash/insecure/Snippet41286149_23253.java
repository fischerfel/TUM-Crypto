import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.File;
import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

/**
 * Created by Ashish Pancholi on 22-12-2016.
 */
public class EncryDecryPtion implements Securable{
    public static void main(String[] argu){
        EncryDecryPtion encryDecryPtion = new EncryDecryPtion();
        File file = new File("shouldbeoriginal.jpg");
        try {
            encryDecryPtion.encryptFile(file,"Pa$$w0rd");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        }

        File file_ = new File("shouldbeoriginal.jpg");
        try {
            encryDecryPtion.decryptFile(file_,"Pa$$w0rd");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        }
    }
} 




import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

/**
 * Encrypt and decrypt file with AES algorithm
 * Created by Ashish Pancholi on 20-12-2016.
 */
public interface Securable {

    /**
     * Read and write the file in chunk.
     * Encrypts the chunks with AES algorithm.
     * It creates a new a file which having encrypted data,
     * deletes old original file and
     * rename a new file with the old file
     * @param file which is to be encrypted and password.
     */

    default File encryptFile(File file, String password) throws IOException, NoSuchAlgorithmException, IllegalBlockSizeException, InvalidKeyException, BadPaddingException, NoSuchPaddingException {
        AESEncryptionDecryption aesEncryptionDecryption = new AESEncryptionDecryption(password);
        String encryptedFilePath = file.getAbsolutePath() + ".ENCRYPTED";
        File encryptedFile = new File(encryptedFilePath);
        encryptedFile.createNewFile();
        try
                (FileInputStream in = new FileInputStream(file)) {

            try
                    (OutputStream out = new FileOutputStream(encryptedFile)) {
                byte[] chunk = new byte[1024];
                int chunkLen = 0;
                while ((chunkLen = in.read(chunk)) != -1) {
                    byte[] encryptedChunk = aesEncryptionDecryption.encrypt(chunk);
                    out.write(encryptedChunk);
                }
            }
        }
        Path path_originalFile = Paths.get(file.getAbsolutePath());
        Path path_encryptedFile = Paths.get(encryptedFile.getAbsolutePath());
        try {
            Files.delete(path_originalFile);
        }catch (IOException ex){
            try {
                FileUtils.forceDelete(file);
            }catch (IOException ex1){
                //ignore
            }
        }
        Path path = Files.move(path_encryptedFile, path_originalFile);
        return path.toFile();
    }

    default File encryptWholeFile(File file, String password) throws IOException, NoSuchAlgorithmException, IllegalBlockSizeException, InvalidKeyException, BadPaddingException, NoSuchPaddingException {
        AESEncryptionDecryption aesEncryptionDecryption = new AESEncryptionDecryption(password);
        String encryptedFilePath = file.getAbsolutePath() + ".ENCRYPTED";
        File encryptedFile = new File(encryptedFilePath);
        encryptedFile.createNewFile();
        try(FileInputStream in = new FileInputStream(file)) {
            byte[] bytes = IOUtils.toByteArray(in);
            byte[] encryptedChunk = aesEncryptionDecryption.encrypt(bytes);
            FileUtils.writeByteArrayToFile(encryptedFile, encryptedChunk);
        }
        Path path_originalFile = Paths.get(file.getAbsolutePath());
        Path path_encryptedFile = Paths.get(encryptedFile.getAbsolutePath());
        try {
            Files.delete(path_originalFile);
        }catch (IOException ex){
            try {
                FileUtils.forceDelete(file);
            }catch (IOException ex1){
                //ignore
            }
        }
        Path path = Files.move(path_encryptedFile, path_originalFile);
        return path.toFile();
    }

    /**
     * Read and write the file in chunk.
     * Encrypts the chunks with AES algorithm.
     * It creates a new a file which having encrypted data,
     * deletes old original file and
     * rename a new file with the old file
     * @param inputStream of file which is to be encrypted and a password.
     */
    default InputStream encryptFile(InputStream inputStream, String password) throws IOException, NoSuchAlgorithmException, IllegalBlockSizeException, InvalidKeyException, BadPaddingException, NoSuchPaddingException {
        InputStream in;
        try {
            AESEncryptionDecryption aesEncryptionDecryption = new AESEncryptionDecryption(password);
            try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
                byte[] chunk = new byte[1024];
                int chunkLen = 0;
                while ((chunkLen = inputStream.read(chunk)) != -1) {
                    byte[] encryptedChunk = aesEncryptionDecryption.encrypt(chunk);
                    baos.write(encryptedChunk);
                }
                baos.flush();
                in = new ByteArrayInputStream(baos.toByteArray());
            }
        }finally {
            inputStream.close();
        }
        return in;
    }

    /**
     * Read and write the file in chunk.
     * Encrypts the chunks with AES algorithm.
     * It creates a new a file which having encrypted data,
     * deletes old original file and
     * rename a new file with the old file
     * @param inputStream of file which is to be encrypted and a password.
     */
    default File encryptFile(InputStream inputStream, String password, String targetFileName) throws IOException, NoSuchAlgorithmException, IllegalBlockSizeException, InvalidKeyException, BadPaddingException, NoSuchPaddingException {
        File encryptedFile = new File(targetFileName);
        try {
            AESEncryptionDecryption aesEncryptionDecryption = new AESEncryptionDecryption(password);
            encryptedFile.getParentFile().mkdirs();
            encryptedFile.createNewFile();
            try (OutputStream baos = new FileOutputStream(encryptedFile)) {
                byte[] chunk = new byte[1024];
                int chunkLen = 0;
                while ((chunkLen = inputStream.read(chunk)) != -1) {
                    byte[] encryptedChunk = aesEncryptionDecryption.encrypt(chunk);
                    baos.write(encryptedChunk);
                }
            }
        }finally {
            inputStream.close();
        }
        return encryptedFile;

    }

    /**
     * Read and write the file in chunk.
     * Decrypts the chunks with AES algorithm.
     * It creates a new a file which having decrypted data,
     * deletes old original encrypted file and
     * rename a new file with the old file
     * @param file which is to be decrypted and password.
     */

    default void decryptFile(File file, String password) throws IOException, NoSuchAlgorithmException, IllegalBlockSizeException, InvalidKeyException, BadPaddingException, NoSuchPaddingException {
        AESEncryptionDecryption aesEncryptionDecryption = new AESEncryptionDecryption(password);
        String decryptedFilePath = file.getAbsolutePath() + ".DECRYPTED";
        File decryptedFile = new File(decryptedFilePath);
        decryptedFile.createNewFile();
        try
                (FileInputStream in = new FileInputStream(file)) {

            try
                    (OutputStream out = new FileOutputStream(decryptedFile)) {
                byte[] chunk = new byte[1024];
                int chunkLen = 0;
                while ((chunkLen = in.read(chunk)) != -1) {
                    byte[] encryptedChunk = aesEncryptionDecryption.decrypt(chunk);
                    out.write(encryptedChunk);
                }
            }
        }
        Path path_originalFile = Paths.get(file.getAbsolutePath());
        Path path_decryptedFile = Paths.get(decryptedFile.getAbsolutePath());
        try {
            Files.delete(path_originalFile);
        }catch (IOException ex){
            try {
                FileUtils.forceDelete(file);
            }catch (IOException ex1){
                //ignore
            }
        }
        Files.move(path_decryptedFile, path_originalFile);
    }

    default File decryptWholeFile(File file, String password) throws IOException, NoSuchAlgorithmException, IllegalBlockSizeException, InvalidKeyException, BadPaddingException, NoSuchPaddingException {
        AESEncryptionDecryption aesEncryptionDecryption = new AESEncryptionDecryption(password);
        String decryptedFilePath = file.getAbsolutePath() + ".DECRYPTED";
        File decryptedFile = new File(decryptedFilePath);
        decryptedFile.createNewFile();
        try(FileInputStream in = new FileInputStream(file)) {
                byte[] bytes = IOUtils.toByteArray(in);
                byte[] encryptedChunk = aesEncryptionDecryption.decrypt(bytes);
                FileUtils.writeByteArrayToFile(decryptedFile, encryptedChunk);
          }
        Path path_originalFile = Paths.get(file.getAbsolutePath());
        Path path_decryptedFile = Paths.get(decryptedFile.getAbsolutePath());
        try {
            Files.delete(path_originalFile);
        }catch (IOException ex){
            try {
                FileUtils.forceDelete(file);
            }catch (IOException ex1){
                //ignore
            }
        }
       Path path =  Files.move(path_decryptedFile, path_originalFile);
        return path.toFile();
    }

}


import org.apache.commons.io.FileUtils;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;


/**
 * Encrypt and decrypt file with AES algorithm
 * Created by Ashish Pancholi on 20-12-2016.
 */

public class AESEncryptionDecryption {

    private SecretKeySpec secretKey;
    private byte[] key;

    public AESEncryptionDecryption(String password) throws NoSuchAlgorithmException, UnsupportedEncodingException{
        MessageDigest sha = null;
        key = password.getBytes("UTF-8");
        sha = MessageDigest.getInstance("SHA-1");
        key = sha.digest(key);
        key = Arrays.copyOf(key, 16); // use only first 128 bit
        this.secretKey = new SecretKeySpec(key, "AES");
    }

    /**
     * Encrypts the file with AES algorithm
     * @param bytes of file which is to encrypted
     * @return byte[] which is encrypted bytes
     */
    public byte[] encrypt(byte[] bytes) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, this.secretKey);
            byte[] encrytedBytes = cipher.doFinal(bytes);
            return encrytedBytes;

    }

    /**
     * Decrypts the file with AES algorithm
     * @param encrytedBytes of file that to be decrypted
     * @return byte[] which is original data.
     */
    public byte[] decrypt(byte[] encrytedBytes) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException
    {
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5PADDING");
        cipher.init(Cipher.DECRYPT_MODE, this.secretKey);
        byte[] bytes = cipher.doFinal(encrytedBytes);
        return bytes;
    }
}
