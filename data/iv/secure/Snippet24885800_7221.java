import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.AlgorithmParameters;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.InvalidParameterSpecException;
import java.security.spec.KeySpec;
import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestAES {

    private static final Logger logger = LoggerFactory.getLogger(TestAES.class);
    private char[] password = "1111111111111111".toCharArray();
    private byte[] salt = "SampleSalt".getBytes();
    private byte[] initializationVector;

    public static void main(String[] args) {
        TestAES aes = new TestAES();

        aes.encrypt(new File("D:\\plainText.licence"), new File("D:\\sample.licence"));
        aes.decrypt(new File("D:\\sample.licence"));
    }

    public void encrypt(File plainTextFile, File encryptedLicenceFile) {

        if (plainTextFile.exists() == false) {
            try {
                throw new FileNotFoundException("The plainTextFile was not found");
            } catch (FileNotFoundException e) {
                logger.error("The plainTextFile was not found", e);
                e.printStackTrace();
            }
        }

        if (encryptedLicenceFile.exists() == false) {
            try {
                encryptedLicenceFile.createNewFile();
            } catch (IOException e) {
                logger.error("IOException while creating encryptedLicenceFile", e);
                e.printStackTrace();
            }
        }

        logger.info("plainTextFile.exists() = " + plainTextFile.exists());

        // To read the file to be encrypted
        FileInputStream fileInputStream = null;

        // To write the encrypted file
        FileOutputStream fileOutputStream = null;

        // To read the file information and to encrypt
        CipherInputStream cipherInputStream = null;
        try {
            fileInputStream = new FileInputStream(plainTextFile);
            fileOutputStream = new FileOutputStream(encryptedLicenceFile);

            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            KeySpec keySpec = new PBEKeySpec(password, salt, 65536, 128);
            logger.info("Generating KeySpec with password, salt.");

            SecretKey secretKey = factory.generateSecret(keySpec);
            SecretKey secret = new SecretKeySpec(secretKey.getEncoded(), "AES");

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            logger.debug("Obtaining AES cipher in encryption mode with secret key.");
            cipher.init(Cipher.ENCRYPT_MODE, secret);
            AlgorithmParameters parameters = cipher.getParameters();
            this.initializationVector = parameters.getParameterSpec(IvParameterSpec.class).getIV();
            logger.info("Initialization Vector length = " + initializationVector.length);

            cipherInputStream = new CipherInputStream(fileInputStream, cipher);

            int read = 0;
            while ((read = cipherInputStream.read()) != -1) {
                fileOutputStream.write((char) read);
                fileOutputStream.flush();
            }

            logger.info("Successfully written the sample.licence file.");

        } catch (NoSuchAlgorithmException e) {
            logger.error("NoSuchAlgorithmException: Check alogrithm.", e);
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            logger.error("NoSuchPaddingException: Check padding.", e);
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            logger.error("InvalidKeySpecException: Check keySpec.", e);
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            logger.error("InvalidKeyException: Check keySize.", e);
            e.printStackTrace();
        } catch (InvalidParameterSpecException e) {
            logger.error("InvalidParameterSpecException: Check parameters.", e);
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            logger.error("FileNotFoundException: Check fileToBeEncrypted.", e);
            e.printStackTrace();
        } catch (IOException e) {
            logger.error("IOException occured.", e);
            e.printStackTrace();
        } finally {
            try {
                if (cipherInputStream != null) {
                    cipherInputStream.close();
                }
                if (fileOutputStream != null) {
                    fileOutputStream.close();
                }
            } catch (IOException e) {
                logger.error("Exception while closing stream.", e);
                e.printStackTrace();
            }
        }

    }

    public void decrypt(File sampleLicence) {

        if (sampleLicence.exists() == false) {
            try {
                throw new FileNotFoundException("The sampleLicence was not found");
            } catch (FileNotFoundException e) {
                logger.error("The sampleLicence was not found", e);
                e.printStackTrace();
            }
        }

        // To read the sample.licence
        FileInputStream fileInputStream = null;

        // To write decrypted licence file
        FileOutputStream fileOutputStream = null;

        // To read encrypted licence file and decrypt it
        CipherOutputStream cipherOutputStream = null;
        ByteArrayOutputStream byteArrayOutputStream = null;
        try {
            fileInputStream = new FileInputStream(sampleLicence);
            byteArrayOutputStream = new ByteArrayOutputStream();

            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            KeySpec keySpec = new PBEKeySpec(password, salt, 65536, 128);
            logger.info("Generating KeySpec with password, salt during decryption.");

            SecretKey secretKey = factory.generateSecret(keySpec);
            SecretKey secret = new SecretKeySpec(secretKey.getEncoded(), "AES");

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            logger.debug("Obtaining AES cipher in decryption mode with secret key.");
            cipher.init(Cipher.DECRYPT_MODE, secret, new IvParameterSpec(initializationVector));

            cipherOutputStream = new CipherOutputStream(byteArrayOutputStream, cipher);
            byte[] data = new byte[4096];
            int read;
            while ((read = fileInputStream.read(data)) != -1) {
                cipherOutputStream.write(data, 0, read);
            }
            cipherOutputStream.flush();

            byte[] plainText = byteArrayOutputStream.toByteArray();
            System.out.println(new String(plainText, "UTF-8"));
        } catch (NoSuchAlgorithmException e) {
            logger.error("NoSuchAlgorithmException: Check alogrithm.", e);
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            logger.error("NoSuchPaddingException: Check padding.", e);
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            logger.error("InvalidKeySpecException: Check keySpec.", e);
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            logger.error("InvalidKeyException: Check keySize.", e);
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            logger.error("FileNotFoundException: Check fileToBeEncrypted.", e);
            e.printStackTrace();
        } catch (IOException e) {
            logger.error("IOException occured.", e);
            e.printStackTrace();
        } catch (InvalidAlgorithmParameterException e) {
            logger.error("InvalidAlgorithmParameterException", e);
            e.printStackTrace();
        } finally {
            try {
                if (cipherOutputStream != null) {
                    cipherOutputStream.close();
                }
                if (fileOutputStream != null) {
                    fileOutputStream.close();
                }
                if (byteArrayOutputStream != null) {
                    byteArrayOutputStream.close();
                }
            } catch (IOException e) {
                logger.error("Exception while closing stream.", e);
                e.printStackTrace();
            }
        }

    }
}
