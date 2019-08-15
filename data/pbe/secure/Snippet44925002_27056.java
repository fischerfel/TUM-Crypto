import javax.crypto.Cipher;
import javax.crypto.EncryptedPrivateKeyInfo;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;
import java.util.List;

public class Main {

    private static byte [] pemFileToBytes(String filename) throws IOException {
        // read in PEM file, throw away the begin and end lines
        List<String> pemLines = Files.readAllLines(Paths.get(filename), StandardCharsets.US_ASCII);
        pemLines.remove(0);
        pemLines.remove(pemLines.size() - 1);
        String pem = String.join("", pemLines);

        // base64 decode and return the result.

        return Base64.getDecoder().decode(pem);
    }

    private static PrivateKey parsePrivateKey (String filename, char [] password) throws Exception{
        PBEKeySpec passKeySpec = new PBEKeySpec(password); //my password

        EncryptedPrivateKeyInfo encryptedKey = new EncryptedPrivateKeyInfo(pemFileToBytes(filename));
        SecretKeyFactory keyFac = SecretKeyFactory.getInstance(encryptedKey.getAlgName());
        SecretKey passKey = keyFac.generateSecret(passKeySpec);

        // Create PBE Cipher
        Cipher pbeCipher = Cipher.getInstance(encryptedKey.getAlgName());
        // Initialize PBE Cipher with key and parameters
        pbeCipher.init(Cipher.DECRYPT_MODE, passKey, encryptedKey.getAlgParameters());

        // Decrypt the private key

        byte [] encodedPrivateKey = pbeCipher.doFinal(encryptedKey.getEncryptedData());
        PKCS8EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec(encodedPrivateKey);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        return kf.generatePrivate(privateKeySpec);

    }

    public static void main(String[] args) throws Exception {
        PrivateKey pk = parsePrivateKey("x.pk8", "pass".toCharArray());
    }
}
