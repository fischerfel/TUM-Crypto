 import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;


    class MyTest{
     public static OutputStream aes256CBCEncrypt(OutputStream os, String passPhrase) throws NoSuchAlgorithmException, NoSuchPaddingException, IOException, InvalidKeyException, InvalidAlgorithmParameterException
    {

        //  MessageDigest md = MessageDigest.getInstance("SHA-256");
        //  md.update(passPhrase.getBytes());
        //  byte[] key = md.digest();

            Cipher aesCipher = Cipher.getInstance("AES/CBC/ISO10126Padding");

            SecureRandom secureRandom = new SecureRandom();
            secureRandom.setSeed(System.currentTimeMillis());
            byte[] bb = new byte[16];
            secureRandom.nextBytes(bb);
            os.write(bb);

            aesCipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(passPhrase.getBytes(), "AES"), new IvParameterSpec(
                    bb));
            return new CipherOutputStream(os, aesCipher);


    }

    public static InputStream aes256CBCDecrypt(File f, String passPhrase)
            throws FileNotFoundException
    {
        FileInputStream fis = null;
        try
        {
            //MessageDigest md = MessageDigest.getInstance("SHA-256");
        //  md.update(passPhrase.getBytes());
        //  byte[] key = md.digest();

            Cipher aesCipher = Cipher.getInstance("AES/CBC/ISO10126Padding");
            fis = new FileInputStream(f);
            byte[] bb = new byte[16];
            fis.read(bb);
            aesCipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(passPhrase.getBytes(), "AES"), new IvParameterSpec(
                    bb));
            return new CipherInputStream(fis, aesCipher);
        }
        catch (final Exception e)
        {


        }
        return null;
    }

    public static void main(String args[]) throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidAlgorithmParameterException, IOException{
    String keyFile = "C:\\contentProducer" + File.separator + "test";
        String encryptedFile = "C:\\contentProducer" + File.separator + "encryptedtest";
        String decryptedFile = "C:\\contentProducer" + File.separator + "decryptedtest";

        FileInputStream in = new FileInputStream(keyFile);
        FileOutputStream bos = new FileOutputStream(new File(encryptedFile));

    //Call method for Encryption
        OutputStream encryptedBos = aes256CBCEncrypt(bos,"0123456789abcdef");
        int inByte;
        while ((inByte = in.read()) != -1 ) {
            encryptedBos.write(inByte);
        }
        in.close();
        bos.close();
        encryptedBos.close();

    //Call Method for Decryption

        InputStream inputStream = aes256CBCDecrypt(new File(encryptedFile), "0123456789abcdef");

        FileOutputStream deos = new FileOutputStream(new File(decryptedFile));
        while ((inByte = inputStream.read()) != -1 ) {
            deos.write(inByte);
        }
        inputStream.close();
        deos.close();

        }

}
