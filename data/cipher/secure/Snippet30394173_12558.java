import android.test.InstrumentationTestCase;

import com.google.protobuf.ByteString;
import bit.Twiddling;

import java.security.AlgorithmParameters;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class CryptoTest extends InstrumentationTestCase {

    public void test_encrypt_and_decrypt() throws Exception {
        byte[] plainText = "a secret message which has more than sixteen bytes".getBytes();
        byte[] key = Twiddling.hexStringBEToBytes("fedcba9876543210fedcba9876543210fedcba9876543210fedcba9876543210");
        byte[] iv = Twiddling.hexStringBEToBytes("0123456789abcdef0123456789abcdef");//0123456789abcdef0123456789abcdef");

        SecretKeySpec aesSecret = new SecretKeySpec(key, "AES");
        Cipher cipher = Cipher.getInstance("AES/CFB/NoPadding");
        Cipher decipher = Cipher.getInstance("AES/CFB/NoPadding");
        IvParameterSpec ivps = new IvParameterSpec(iv);
        assertEquals(16, iv.length);

        cipher.init(Cipher.ENCRYPT_MODE, aesSecret, ivps);
        byte[] cipherText = cipher.doFinal(plainText);

        assertEquals(plainText.length, cipherText.length);
        assertEquals("05C2AAD7BAC42ED084739340D47CEC9F03D8E94AC7B1E11A56A6654F76AD2C8076BCA162303E39B44D043732E98FDD28C52D", Twiddling.bytesToHexStringBE(cipherText));

        decipher.init(Cipher.DECRYPT_MODE, aesSecret, ivps);
        byte[] deciphered = decipher.doFinal(cipherText);

        assertEquals(new String(plainText), new String(deciphered));
    }
}
