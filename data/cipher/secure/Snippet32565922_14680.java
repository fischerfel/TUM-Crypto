import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.util.Arrays;
import java.util.Base64;
import java.util.Base64.Decoder;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class TestAesDecrypt {

    public static void main(final String[] args) throws Exception {
        final byte[] pass = "testpass".getBytes(StandardCharsets.US_ASCII);
        final byte[] magic = "Salted__".getBytes(StandardCharsets.US_ASCII);
        final String inFile = "e:/t/e.txt";

        String source = new String(Files.readAllBytes(Paths.get(inFile)),
                StandardCharsets.US_ASCII);
        source = source.replaceAll("\\s", "");
        final Decoder decoder = Base64.getDecoder();
        final byte[] inBytes = decoder.decode(source);

        final byte[] shouldBeMagic = Arrays.copyOfRange(inBytes, 0,
                magic.length);
        if (!Arrays.equals(shouldBeMagic, magic)) {
            System.out.println("Bad magic number");
            return;
        }

        final byte[] salt = Arrays.copyOfRange(inBytes, magic.length,
                magic.length + 8);

        final byte[] passAndSalt = concat(pass, salt);

        byte[] hash = new byte[0];
        byte[] keyAndIv = new byte[0];
        for (int i = 0; i < 3; i++) {
            final byte[] data = concat(hash, passAndSalt);
            final MessageDigest md = MessageDigest.getInstance("MD5");
            hash = md.digest(data);
            keyAndIv = concat(keyAndIv, hash);
        }

        final byte[] keyValue = Arrays.copyOfRange(keyAndIv, 0, 32);
        final byte[] iv = Arrays.copyOfRange(keyAndIv, 32, 48);
        final Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        final SecretKeySpec key = new SecretKeySpec(keyValue, "AES");
        cipher.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(iv));
        final byte[] clear = cipher.doFinal(inBytes, 16, inBytes.length - 16);
        final String clearText = new String(clear, StandardCharsets.ISO_8859_1);
        System.out.println(clearText);
    }

    private static byte[] concat(final byte[] a, final byte[] b) {
        final byte[] c = new byte[a.length + b.length];
        System.arraycopy(a, 0, c, 0, a.length);
        System.arraycopy(b, 0, c, a.length, b.length);
        return c;
    }
}
