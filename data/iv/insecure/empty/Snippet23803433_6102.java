import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.Security;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.ShortBufferException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.io.output.ByteArrayOutputStream;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import com.fiberlink.security.crypto.CryptoUtils;

public class DecryptDocsController {

    static {
        Security.addProvider(new BouncyCastleProvider());
    }
    public static final String PKCS7_PADDING = "AES/CBC/PKCS7Padding";
    public static final int CHUNK_SIZE = 16;
    public static final int STARTING_LOCATION = 0;
    public static final int STREAM_FINISH_LOCATION = -1;
    public void encryptFile() throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException, IOException, IllegalBlockSizeException, BadPaddingException 
        {
        FileInputStream fis = new FileInputStream(DECRYPTED_FILE_LOCATION_1);
        FileOutputStream fos = new FileOutputStream(ENC_FILE_LOCATION_1);
        Cipher cipher = Cipher.getInstance(PKCS7_PADDING);
        SecretKeySpec skeySpec = new SecretKeySpec(getEcryptionByte(FILE_ENCRYPION_KEY), "AES");
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec, new IvParameterSpec(new byte[16]));
        CipherInputStream inputStream = new CipherInputStream(fis, cipher);
        int count =0;
        byte[] data = new byte[CHUNK_SIZE];
        while((count=(inputStream.read(data, STARTING_LOCATION, CHUNK_SIZE))) != STREAM_FINISH_LOCATION) {
            fos.write(data, STARTING_LOCATION, count);
        }
        fis.close();
        fos.close();
        inputStream.close();
       }

    public void decryptFile() throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidAlgorithmParameterException, IOException, IllegalBlockSizeException, BadPaddingException 
        {
        FileInputStream fis = new FileInputStream(ENC_FILE_LOCATION_2);
        FileOutputStream fos = new FileOutputStream(DECRYPTED_FILE_LOCATION_2);
        Cipher cipher = Cipher.getInstance(PKCS7_PADDING);
        SecretKeySpec skeySpec = new SecretKeySpec(getEcryptionByte(FILE_ENCRYPION_KEY), "AES");
        cipher.init(Cipher.DECRYPT_MODE, skeySpec, new IvParameterSpec(new byte[16]));
        CipherInputStream inputStream = new CipherInputStream(fis, cipher);
        int count =0;
        byte[] data = new byte[CHUNK_SIZE];
        while((count=(inputStream.read(data, STARTING_LOCATION, CHUNK_SIZE))) != STREAM_FINISH_LOCATION) {
            fos.write(data, STARTING_LOCATION, count);`enter code here`
        }
        fis.close();
        fos.close();
        inputStream.close();
        }
}
