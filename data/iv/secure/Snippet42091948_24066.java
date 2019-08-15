package so;

import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.bouncycastle.util.encoders.Hex;

public class AESdecryptor {
    public static String[] KeyIvEncrypted = new String[]{
            new String("0123456789abcdef"),
            new String("0000000000000000"),
            new String("1ff4ec7cef0e00d81b2d55a4bfdad4ba")
            };

    public static void main(String[] args) throws GeneralSecurityException {
        // Security.addProvider(new BouncyCastleProvider());
        byte[] decrypted = decrypt();
        System.out.println(new String(decrypted, StandardCharsets.ISO_8859_1));
    }

    private static byte[] transform(int mode, byte[] keyBytes, byte[] ivBytes, byte[] messageBytes)
            throws GeneralSecurityException {
        final SecretKeySpec keySpec = new SecretKeySpec(keyBytes, "AES");
        final IvParameterSpec ivSpec = new IvParameterSpec(ivBytes);
        final Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(mode, keySpec, ivSpec);
        return cipher.doFinal(messageBytes);
    }

    public static byte[] decrypt() throws GeneralSecurityException {
         return AESdecryptor.transform(Cipher.DECRYPT_MODE, KeyIvEncrypted[0].getBytes(), KeyIvEncrypted[1].getBytes(), Hex.decode(KeyIvEncrypted[2]));
    }
}
