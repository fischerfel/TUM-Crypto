import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;


public class AppendAES {

    public static void appendAES(File file, byte[] data, byte[] key) throws IOException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
        RandomAccessFile rfile = new RandomAccessFile(file,"rw");
        byte[] iv = new byte[16];
        byte[] lastBlock = null;
        if (rfile.length() % 16L != 0L) {
            throw new IllegalArgumentException("Invalid file length (not a multiple of block size)");
        } else if (rfile.length() == 16) {
            throw new IllegalArgumentException("Invalid file length (need 2 blocks for iv and data)");
        } else if (rfile.length() == 0L) { 
            // new file: start by appending an IV
            new SecureRandom().nextBytes(iv);
            rfile.write(iv);
            // we have our iv, and there's no prior data to reencrypt
        } else { 
            // file length is at least 2 blocks
            rfile.seek(rfile.length()-32); // second to last block
            rfile.read(iv); // get iv
            byte[] lastBlockEnc = new byte[16]; 
                // last block
                // it's padded, so we'll decrypt it and 
                // save it for the beginning of our data
            rfile.read(lastBlockEnc);
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(key,"AES"), new IvParameterSpec(iv));
            lastBlock = cipher.doFinal(lastBlockEnc);
            rfile.seek(rfile.length()-16); 
                // position ourselves to overwrite the last block
        } 
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(key,"AES"), new IvParameterSpec(iv));
        byte[] out;
        if (lastBlock != null) { // lastBlock is null if we're starting a new file
            out = cipher.update(lastBlock);
            if (out != null) rfile.write(out);
        }
        out = cipher.doFinal(data);
        rfile.write(out);
        rfile.close();
    }

    public static void decryptAES(File file, OutputStream out, byte[] key) throws IOException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
        // nothing special here, decrypt as usual
        FileInputStream fin = new FileInputStream(file);
        byte[] iv = new byte[16];
        if (fin.read(iv) < 16) {
            throw new IllegalArgumentException("Invalid file length (needs a full block for iv)");
        };
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(key,"AES"), new IvParameterSpec(iv));
        byte[] buff = new byte[1<<13]; //8kiB
        while (true) {
            int count = fin.read(buff);
            if (count == buff.length) {
                out.write(cipher.update(buff));
            } else {
                out.write(cipher.doFinal(buff,0,count));
                break;
            }
        }
        fin.close();
    }

    public static void main(String[] args) throws Exception {
        byte[] key = new byte[]{0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15};
        for (int i = 0; i<1000; i++) {
            appendAES(new File("log.aes"),"All work and no play makes Jack a dull boy. ".getBytes("UTF-8"),key);
        }
        decryptAES(new File("log.aes"), new FileOutputStream("plain.txt"), key);
    }

}
