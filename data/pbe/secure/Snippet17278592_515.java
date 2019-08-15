import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.Provider;
import java.security.spec.InvalidKeySpecException;
import java.util.Random;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import sun.misc.BASE64Encoder;


public class OpenSSLEncryption {

    private static final String CIPHER_ALG = "PBEWITHMD5AND256BITAES-CBC-OPENSSL";
    private static final Provider CIPHER_PROVIDER = new BouncyCastleProvider();
    private static final String PREFIX = "Salted__";
    private static final String UTF_8 = "UTF-8";
    private String password;
    private PBEKeySpec pbeSpec;
    private SecretKeyFactory keyFact;
    private Cipher cipher;
    private Random rand = new Random();
    private BASE64Encoder encoder = new BASE64Encoder();

    public OpenSSLEncryption(String password) throws NoSuchAlgorithmException, NoSuchPaddingException {
        this.password = password;
        pbeSpec = new PBEKeySpec(password.toCharArray());
        keyFact = SecretKeyFactory.getInstance(CIPHER_ALG, CIPHER_PROVIDER);
        cipher = Cipher.getInstance(CIPHER_ALG, CIPHER_PROVIDER);
    }  

    public synchronized String encrypt(String toEncrypt) throws InvalidKeySpecException, InvalidKeyException, InvalidAlgorithmParameterException, UnsupportedEncodingException, IllegalBlockSizeException, BadPaddingException, IOException {
        byte[] salt = new byte[8];
        rand.nextBytes(salt);
        PBEParameterSpec defParams = new PBEParameterSpec(salt, 0);
        cipher.init(Cipher.ENCRYPT_MODE, keyFact.generateSecret(pbeSpec), defParams);
        byte[] cipherText = cipher.doFinal(toEncrypt.getBytes(UTF_8));

        ByteArrayOutputStream baos = new ByteArrayOutputStream(cipherText.length + 16);
        baos.write(PREFIX.getBytes(UTF_8));
        baos.write(salt);
        baos.write(cipherText);
        baos.close();
        return encoder.encode(baos.toByteArray());
    }
}
