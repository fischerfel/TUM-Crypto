import org.bouncycastle.jce.provider.BouncyCastleProvider;

import javax.crypto.Cipher;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.security.Security;

public class Main {

    // Test Bytes - 16 bytes
    static byte[] testInput = {0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07,
            0x08, 0x09, 0x0a, 0x0b, 0x0c, 0x0d, 0x0e, 0x0f};

    // Test key - 16 bytes, 128-bit
    static byte[] keyBytes = {0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07,
            0x08, 0x09, 0x0a, 0x0b, 0x0c, 0x0d, 0x0e, 0x0f};

    // Test nonce - 13 bytes, 104-bit
    static byte[] nonce = {0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, 0x09,
            0x0a, 0x0b, 0x0c};


    public static void main(String[] args) throws Exception {
        Security.addProvider(new BouncyCastleProvider());
        GCMParameterSpec parameterSpec = new GCMParameterSpec(32, nonce);
        Cipher cipher = Cipher.getInstance("AES/CCM/NoPadding");
        SecretKeySpec secretKeySpec = new SecretKeySpec(keyBytes, "AES");
        cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, parameterSpec);
        cipher.updateAAD(new byte[]{0x01});
        System.out.println(DatatypeConverter.printHexBinary(cipher.doFinal(testInput)));
    }
}
