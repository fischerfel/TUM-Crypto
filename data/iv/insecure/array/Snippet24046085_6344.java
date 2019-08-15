import java.io.*;
import java.security.*;
import java.math.*;

import javax.crypto.*;
import javax.crypto.spec.*;

import java.security.spec.*;
import java.util.*;
import java.util.zip.*;

class InputStreamWithZipMd5Aes extends InputStream
{
        InputStream is;
        MessageDigest md = MessageDigest.getInstance("MD5");
        MessageDigest mdEncrytped = MessageDigest.getInstance("MD5");

        public InputStreamWithZipMd5Aes(InputStream innerInputStream, String password) throws Exception
        {
            if (password==null)
                throw new IllegalArgumentException("password");



        byte[] key = password.getBytes("UTF-8");
        MessageDigest sha = MessageDigest.getInstance("SHA-1");
        key = sha.digest(key);
        key = Arrays.copyOf(key, 16); // use only first 128 bit
        SecretKeySpec secretKeySpec = new SecretKeySpec(key, "AES");    

        Cipher ecipher = Cipher.getInstance("AES/CBC/PKCS5Padding"); 

        byte[] iv = new byte[]
        {
            0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, 0x09,0x0a, 0x0b, 0x0c, 0x0d, 0x0e, 0x0f
        };

        AlgorithmParameterSpec paramSpec = new IvParameterSpec(iv);                 
        ecipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, paramSpec); 

        //File -> MD5 -> Zip -> Encrypt -> MD5
        is = new DigestInputStream(innerInputStream, md);
        is = new DeflaterInputStream(is); 
        is = new CipherInputStream(is, ecipher); 
        is = new DigestInputStream(is, mdEncrytped);
    }

    public int read() throws IOException
    {
        return is.read();
    }

    public String getMd5()
    {
        BigInteger bi = new BigInteger(1, md.digest());
        return String.format("%1$032X", bi);
    }

    public String getMd5encrypted()
    {
        BigInteger bi = new BigInteger(1, mdEncrytped.digest());
        return String.format("%1$032X", bi);
    }
}

class OutputStreamWithZipMd5Aes extends OutputStream
{
    Cipher chiper;
    OutputStream os;
    MessageDigest md = MessageDigest.getInstance("MD5");

    public OutputStreamWithZipMd5Aes(OutputStream innerOutpuStream, String password) throws Exception
    {
        if (password==null)
            throw new IllegalArgumentException("password");

        byte[] key = password.getBytes("UTF-8");
        MessageDigest sha = MessageDigest.getInstance("SHA-1");
        key = sha.digest(key);
        key = Arrays.copyOf(key, 16); // use only first 128 bit
        SecretKeySpec secretKeySpec = new SecretKeySpec(key, "AES");    

        chiper = Cipher.getInstance("AES/CBC/PKCS5Padding"); 

        byte[] iv = new byte[]
        {
            0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, 0x09,0x0a, 0x0b, 0x0c, 0x0d, 0x0e, 0x0f
        };

        AlgorithmParameterSpec paramSpec = new IvParameterSpec(iv);                 
        chiper.init(Cipher.DECRYPT_MODE, secretKeySpec, paramSpec); 

        // Decrypt -> Unzip -> MD5 -> File
        os = new DigestOutputStream(innerOutpuStream, md);
        os = new InflaterOutputStream(os); 
        os = new CipherOutputStream(os, chiper);
    }

    public void write(int n) throws IOException
    {
        os.write(n);
    }

    public String getMd5()
    {
        BigInteger bi = new BigInteger(1, md.digest());
        return String.format("%1$032X", bi);
    }

    private static void verifyEncryptDecrypt(File file) throws Exception 
    {
        InputStream inClean = new BufferedInputStream(new FileInputStream(file));
        ByteArrayOutputStream bytesClean = new ByteArrayOutputStream();

        InputStream in = new BufferedInputStream(new FileInputStream(file));
        in = new InputStreamWithZipMd5Aes(in, "password");

        ByteArrayOutputStream bytesProcessed = new ByteArrayOutputStream();
        OutputStreamWithZipMd5Aes os = new OutputStreamWithZipMd5Aes(bytesProcessed, "password");

        byte[] buffer = new byte[1024*1024];

        while (true)
        {
            int read = in.read(buffer);
            if (read == -1)
                break;
            os.write(buffer, 0, read);
        }    

        while (true)
        {
            int read = inClean.read(buffer);
            if (read == -1)
                break;
            bytesClean.write(buffer, 0, read);
        }          

        os.close();
        bytesClean.close();


        System.out.println("#1 " + bytesClean.size() + " : " + bytesClean.toString());
        System.out.println("#2 " + bytesProcessed.size() + " : " + bytesProcessed.toString());
        System.out.println(((InputStreamWithZipMd5Aes)in).getMd5() + " == " + ((OutputStreamWithZipMd5Aes)os).getMd5());
    }

    public static void main(String[] a) throws Exception 
    {
        File file1 = new File("c:\\test.html");
        verifyEncryptDecrypt(file1);
    }
}
