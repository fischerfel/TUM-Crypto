import java.security.GeneralSecurityException;
import java.util.Arrays;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.binary.Base64;

public class Decryption {

public static void main(String args[]) {
//I have changed the original key. So mere copy pasting may not work. Put your key here.
    String key = "FfDaaaaaaa444aaaa7aaEFF4A76efaaaaaE5C23F5E4C3adeaaaaaaCAA796E307";
    String encrypted = "8AQ8SvpF1zgyNyxKwLlX\\/cGzwLE5skU58pg3kaSrt+AJt9D7\\/3vaNRPZISIKMdCUwwkQ2nxj8PVABRy0aaeBfsJN9n2Ltco6oPjdcmx8eOI";
    String decrypted = "";
    try {

        try {
            decrypted = decrypt(Hex.decodeHex(key.toCharArray()), encrypted);
        } catch (DecoderException e) {
            e.printStackTrace();
        }

        System.out.println(decrypted);
    } catch (GeneralSecurityException e) {
        e.printStackTrace();
    }

}

public static String decrypt(byte key[], String encrypted)
        throws GeneralSecurityException {
    /*
     * if (key.length != 32 || key.length != 48 || key.length != 64) { throw
     * new IllegalArgumentException("Invalid key size."); }
     */
    byte[] ciphertextBytes = Base64.decodeBase64(encrypted.getBytes());

    IvParameterSpec iv = new IvParameterSpec(ciphertextBytes, 0, 16);

    ciphertextBytes = Arrays.copyOfRange(ciphertextBytes, 16,
            ciphertextBytes.length);

    SecretKeySpec skeySpec = new SecretKeySpec(key, "AES");

    Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
    cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);
    byte[] original = cipher.doFinal(ciphertextBytes);

    // Remove zero bytes at the end.
    int lastLength = original.length;
    for (int i = original.length - 1; i > original.length - 16; i--) {
        if (original[i] == (byte) 0) {
            lastLength--;
        } else {
            break;
        }
    }

    return new String(original, 0, lastLength); 

}

}
