import java.nio.charset.Charset;
import java.security.GeneralSecurityException;
import java.security.InvalidKeyException;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.lang3.StringUtils;

public class GenPass {

    public static void main(String[] args) {
        String secret = "secret"; /* same secret */
        String password = args[0];
        if (args.length > 1) {
            secret = args[1];
        }
        System.out.println("Using secret: [" + secret + "]");
        System.out.println("Password [" + password + "]");
        String encodePassword = new GenPass().encodePassword(password, secret);
        System.out.println("Encoded password [" + encodePassword + "]");
    }

    private String encodePassword(String password, String secret) {
        try {
            final SecretKeySpec secretKey = new SecretKeySpec(this.decodeHex(secret.toCharArray()), "AES"); /* same set*/
            final Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding"); // same set here

            cipher.init(Cipher.ENCRYPT_MODE, secretKey, new IvParameterSpec(new byte[16]));

            String paddedString = StringUtils.left(StringUtils.rightPad(password, 16, (char) 0), 16);

            byte[] encryptedBytes = cipher.doFinal(paddedString.getBytes(Charset.forName("UTF-8")));
            return Base64.getEncoder().encodeToString(encryptedBytes);

        } catch (InvalidKeyException invalidKeyException) {
            System.out.println("Chave incorreta durante criptografia da senha" + invalidKeyException);
            throw new RuntimeException(invalidKeyException);

        } catch (GeneralSecurityException generalSecurityException) {
            System.out.println("Falha inesperada durante criptografia da senha" + generalSecurityException.toString());
            throw new RuntimeException(generalSecurityException);
        }

    }

    private String normalize(String password) {
        char[] array = new char[16];
        char[] temp = password.toCharArray();
        System.arraycopy(temp, 0, array, 0, 16);
        return String.valueOf(array);
    }

    public static byte[] decodeHex(final char[] data) {

        final int len = data.length;

        if ((len & 0x01) != 0) {
            throw new RuntimeException("Odd number of characters.");
        }

        final byte[] out = new byte[len >> 1];

        // two characters form the hex value.
        for (int i = 0, j = 0; j < len; i++) {
            int f = toDigit(data[j], j) << 4;
            j++;
            f = f | toDigit(data[j], j);
            j++;
            out[i] = (byte) (f & 0xFF);
        }

        return out;
    }

    protected static int toDigit(final char ch, final int index) {
        final int digit = Character.digit(ch, 16);
        if (digit == -1) {
            throw new RuntimeException("Illegal hexadecimal character " + ch + " at index " + index);
        }
        return digit;
    }
}
