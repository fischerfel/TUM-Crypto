import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

public class Crypter {

    Cipher ecipher;
    Cipher dcipher;

    Crypter(String as_Phrase)
            throws UnsupportedEncodingException, NoSuchAlgorithmException, InvalidKeyException, NoSuchPaddingException {
        this.ecipher = Cipher.getInstance("DESede");
        this.dcipher = Cipher.getInstance("DESede");
        this.ecipher.init(1, getSecretKey(as_Phrase));
        this.dcipher.init(2, getSecretKey(as_Phrase));

    }

    public String encrypt(String as_valueToEncrypt)
            throws BadPaddingException, IllegalBlockSizeException, UnsupportedEncodingException, IOException {
        byte[] lbarr_utf8 = as_valueToEncrypt.getBytes("UTF8");
        byte[] lbarr_enc = this.ecipher.doFinal(lbarr_utf8);

        return new BASE64Encoder().encode(lbarr_enc);
    }

    public String decrypt(String as_valueToDecrypt)
            throws BadPaddingException, IllegalBlockSizeException, UnsupportedEncodingException, IOException {
        byte[] lbarr_enc = new BASE64Decoder().decodeBuffer(as_valueToDecrypt);

        byte[] lbarr_utf8 = this.dcipher.doFinal(lbarr_enc);

        return new String(lbarr_utf8, "UTF8");
    }

    private SecretKey getSecretKey(String as_Phrase)
            throws UnsupportedEncodingException {
        return new SecretKeySpec(as_Phrase.getBytes("UTF8"), "DESede");
    }
}
