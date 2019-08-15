import android.util.Base64;
import android.util.Log;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;


public class AES {

    static final String TAG = "SymmetricAlgorithmAES";

    private SecretKeySpec secretKey;
    private byte[] key;

    private String decryptedString;
    private String encryptedString;

    //Sets the value of the key
    public void setKey(String myKey) {

        MessageDigest sha = null;
        try {
            key = myKey.getBytes("UTF-8");
            sha = MessageDigest.getInstance("SHA-1");
            key = sha.digest(key);

            key = Arrays.copyOf(key, 16);
            secretKey = new SecretKeySpec(key, "AES");
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            Log.e(TAG, "Error in setting the key");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    public SecretKeySpec getKey(){
        return secretKey;
    }

    public String getDecryptedString() {
        return decryptedString;
    }

    public void setDecryptedString(String decryptedString) {
        this.decryptedString = decryptedString;
    }

    public String getEncryptedString() {
        return encryptedString;
    }

    public void setEncryptedString(String encryptedString) {
        this.encryptedString = encryptedString;
    }

    //Method for Encryption
    public void encrypt(String strToEncrypt) {
        try {
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            byte[] encd = cipher.doFinal(strToEncrypt.getBytes("UTF-8"));
            byte[] cc =Base64.encode(encd,Base64.DEFAULT);
            setEncryptedString(new String(cc));           
        } catch (Exception e) {
            Log.e(TAG, "Error in Encryption");
        }
    }

    //Method for Decryption
    public void decrypt(String strToDecrypt) {
        byte[] decd = null;
        try {
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5PADDING");
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            decd = cipher.doFinal(strToDecrypt.getBytes("UTF-8"));

        } catch (Exception e) {
            Log.e(TAG, "Error in Decryption" + decd);
        }
        setDecryptedString(decd.toString());
    }
}
