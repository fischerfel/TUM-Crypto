import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.KeySpec;


public class DesEncrypter {
    private Cipher ecipher;

    private Cipher dcipher;

    private byte[] salt = {(byte) 0x10, (byte) 0x1B, (byte) 0x12, (byte) 0x21, (byte) 0xba, (byte) 0x5e,
            (byte) 0x99, (byte) 0x12};

    public DesEncrypter(String passphrase) throws Exception {
        int iterationCount = 2;
        KeySpec keySpec = new PBEKeySpec(passphrase.toCharArray(), salt, iterationCount);
        SecretKey key = SecretKeyFactory.getInstance("PBEWithMD5AndDES").generateSecret(keySpec);
        ecipher = Cipher.getInstance(key.getAlgorithm());
        dcipher = Cipher.getInstance(key.getAlgorithm());

        AlgorithmParameterSpec paramSpec = new PBEParameterSpec(salt, iterationCount);

        ecipher.init(Cipher.ENCRYPT_MODE, key, paramSpec);
        dcipher.init(Cipher.DECRYPT_MODE, key, paramSpec);
    }

    public String encrypt(String str) throws Exception {
        return new BASE64Encoder().encode(ecipher.doFinal(str.getBytes())).trim();
    }

    public String decrypt(String str) throws Exception {
        return new String(dcipher.doFinal(new BASE64Decoder().decodeBuffer(str))).trim();
    }
}
