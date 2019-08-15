import java.math.BigInteger;
import javax.crypto.Cipher;
import javax.crypto.Mac;
import java.security.spec.AlgorithmParameterSpec;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import com.sun.org.apache.xml.internal.security.utils.Base64;

public class Encrypt {
    private byte[] ivMac, ivHotp, sharedKey, macKey;
    private SecretKey secretSharedKey, secretMacKey;
    private Cipher cipher;
    private AlgorithmParameterSpec cipherParamSpec;
    private Mac mac;

    public Encrypt(String encryptionKey, String macKey, String ivMac, String ivHotp, String csvFilePath) throws FileNotFoundException {
        try {
            this.ivMac = hexStr2Bytes(ivMac);
            this.ivHotp = hexStr2Bytes(ivHotp);
            this.sharedKey = hexStr2Bytes(encryptionKey);
            this.macKey = hexStr2Bytes(macKey);         
            this.cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            this.mac = Mac.getInstance("HmacSHA1");
            this.mac.init(new SecretKeySpec(this.macKey, "HmacSHA1"));          
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void encryptSingleSecret(String serialNo, String secret) {
        try {
            byte[] secretBytes = hexStr2Bytes(secret);
            String macKeyString = Base64.encode(macKey);
            cipherParamSpec = new IvParameterSpec(this.ivHotp);
            cipher.init(Cipher.ENCRYPT_MODE, new     SecretKeySpec(this.sharedKey, "AES"), cipherParamSpec);
            byte[] secretDigested = cipher.doFinal(secretBytes);
            cipherParamSpec = new IvParameterSpec(this.ivMac);
            cipher.init(Cipher.ENCRYPT_MODE, new     SecretKeySpec(this.sharedKey, "AES"), cipherParamSpec);
            byte[] macDigested = cipher.doFinal(macKey);
            String MacEncrypted = Base64.encode(macDigested);
            String SecretEncrypted = Base64.encode(secretDigested);
            String MacValue =     Base64.encode(mac.doFinal(secretDigested));
            System.out.println("MAC Key: " + MacEncrypted);
            System.out.println("Secret: " + SecretEncrypted);
            System.out.println("MAC ValueMac: " + MacValue);
            return;
        } catch (Exception ex) {
            ex.printStackTrace();
            return;
        }
    }

    /**
     * From RFC 6238 App. A
     * @param hex
     * @return
     */
    public byte[] hexStr2Bytes(String hex) {
        // Adding one byte to get the right conversion
        // Values starting with "0" can be converted
        byte[] bArray = new BigInteger("10" + hex,16).toByteArray();

        // Copy all the REAL bytes, not the "first"
        byte[] ret = new byte[bArray.length - 1];
        for (int i = 0; i < ret.length; i++)
            ret[i] = bArray[i+1];
        return ret;
    }
}
