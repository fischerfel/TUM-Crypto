import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;


public class AppendAES {
    public static CipherOutputStream appendAES(File file, SecretKeySpec key) throws IOException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
        return appendAES(file, key, null);
    }

    public static CipherOutputStream appendAES(File file, SecretKeySpec key, SecureRandom sr) throws IOException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
        RandomAccessFile rfile = new RandomAccessFile(file,"rw");
        byte[] iv = new byte[16];
        byte[] lastBlock = null;
        if (rfile.length() % 16L != 0L) {
            throw new IllegalArgumentException("Invalid file length (not a multiple of block size)");
        } else if (rfile.length() == 16) {
            throw new IllegalArgumentException("Invalid file length (need 2 blocks for iv and data)");
        } else if (rfile.length() == 0L) { 
            // new file: start by appending an IV
            if (sr == null) sr = new SecureRandom();
            sr.nextBytes(iv);
            rfile.write(iv);
        } else { 
            // file length is at least 2 blocks
            rfile.seek(rfile.length()-32);
            rfile.read(iv);
            byte[] lastBlockEnc = new byte[16];
            rfile.read(lastBlockEnc);
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(iv));
            lastBlock = cipher.doFinal(lastBlockEnc);
            rfile.seek(rfile.length()-16);
        } 
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, key, new IvParameterSpec(iv));
        byte[] out;
        if (lastBlock != null) {
            out = cipher.update(lastBlock);
            if (out != null) rfile.write(out);
        }
        CipherOutputStream cos = new CipherOutputStream(new FileOutputStream(rfile.getFD()),cipher);
        return cos;
    }

    public static CipherInputStream decryptAES(File file, SecretKeySpec key) throws IOException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
        FileInputStream fin = new FileInputStream(file);
        byte[] iv = new byte[16];
        if (fin.read(iv) < 16) {
            throw new IllegalArgumentException("Invalid file length (needs a full block for iv)");
        };
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(iv));
        CipherInputStream cis = new CipherInputStream(fin,cipher);
        return cis;
    }

    public static void main(String[] args) throws Exception {
        byte[] keyBytes = new byte[]{
            0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15
        };
        SecretKeySpec key = new SecretKeySpec(keyBytes,"AES");

        for (int i = 0; i<100; i++) {
            CipherOutputStream cos = appendAES(new File("log.aes"),key);
            cos.write("All work and no play ".getBytes("UTF-8"));
            cos.write("makes Jack a dull boy.  \n".getBytes("UTF-8"));
            cos.close();
        }

        CipherInputStream cis = decryptAES(new File("log.aes"), key);
        BufferedReader bread = new BufferedReader(new InputStreamReader(cis,"UTF-8"));
        System.out.println(bread.readLine());
        cis.close();
    }

}
