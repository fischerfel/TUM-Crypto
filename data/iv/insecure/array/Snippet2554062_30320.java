import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.spec.AlgorithmParameterSpec;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;

// Adapted from http://www.exampledepot.com/egs/javax.crypto/DesFile.html
public class AesEncrypter {

    private Cipher ecipher;
    private Cipher dcipher;

    // Buffer used to transport the bytes from one stream to another
    private byte[] buf = new byte[1024];

    public AesEncrypter(SecretKey key) throws Exception {
 // Create an 8-byte initialization vector
 byte[] iv = new byte[] { (byte) 0x8E, 0x12, 0x39, (byte) 0x9C, 0x07, 0x72, 0x6F, 0x5A };
 AlgorithmParameterSpec paramSpec = new IvParameterSpec(iv);

 ecipher = Cipher.getInstance("AES/CBC/NoPadding");
 dcipher = Cipher.getInstance("AES/CBC/NoPadding");

 // CBC requires an initialization vector
 ecipher.init(Cipher.ENCRYPT_MODE, key, paramSpec);
 dcipher.init(Cipher.DECRYPT_MODE, key, paramSpec);
    }

    public void encrypt(InputStream in, OutputStream out) throws Exception {
 // Bytes written to out will be encrypted
 out = new CipherOutputStream(out, ecipher);

 // Read in the cleartext bytes and write to out to encrypt
 int numRead = 0;
 while ((numRead = in.read(buf)) >= 0) {
     out.write(buf, 0, numRead);
 }
 out.close();
    }

    public void decrypt(InputStream in, OutputStream out) throws Exception {
 // Bytes read from in will be decrypted
 in = new CipherInputStream(in, dcipher);

 // Read in the decrypted bytes and write the cleartext to out
 int numRead = 0;
 while ((numRead = in.read(buf)) >= 0) {
     out.write(buf, 0, numRead);
 }
 out.close();
    }

    public static void main(String[] args) throws Exception {
 System.out.println("Starting...");

 SecretKey key = KeyGenerator.getInstance("AES").generateKey();

 InputStream in = new FileInputStream(new File("/home/wellington/Livros/O Alienista/speechgen0001.mp3/"));
 OutputStream out = System.out;

 AesEncrypter encrypter = new AesEncrypter(key);
 encrypter.encrypt(in, out);

 System.out.println("Done!");
    }

}
