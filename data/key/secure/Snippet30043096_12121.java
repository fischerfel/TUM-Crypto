import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.Security;

import javax.crypto.Cipher;
import javax.crypto.CipherOutputStream;
import javax.crypto.spec.SecretKeySpec;

import org.spongycastle.jce.provider.BouncyCastleProvider;

public class AesHelper {

    private SecretKeySpec key;
    private byte[] input;
    private ByteArrayOutputStream output;
    private CipherOutputStream cipherOutput;
    private Cipher encrypt;

    public AesHelper(byte[] chosenKey, String plaintext) {
        Security.addProvider(new BouncyCastleProvider());
        key = new SecretKeySpec(chosenKey, "AES");
        try {
            encrypt = Cipher.getInstance("AES/CBC/PKCS7Padding", "BC");
            encrypt.init(Cipher.ENCRYPT_MODE, key);
            input = plaintext.getBytes();
        } catch (Exception e) {
            Log.d("testclient", e.getMessage());
        }
    }

    public byte[] encrypt() {
        output = new ByteArrayOutputStream();
        cipherOutput = new CipherOutputStream(output, encrypt);
        try {
            cipherOutput.write(input);
            cipherOutput.close();
        } catch (IOException e) {
            Log.d("testclient", e.getMessage());
        }
        return output.toByteArray();
    }
}
