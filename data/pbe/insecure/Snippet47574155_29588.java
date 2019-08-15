    import java.security.Key;
import java.security.spec.KeySpec;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply=true)
public class CryptoJPAConverter implements AttributeConverter<String, String> {

    private static final String _algorithm = "AES";
    private static final String _password = "_pasword*";
    private static final String _salt = "_salt*";
    private static final String _keygen_spec = "PBKDF2WithHmacSHA1";
    private static final String _cipher_spec = "AES/ECB/PKCS5Padding";

    @Override
    public String convertToDatabaseColumn(String clearText) {
        Key key;
        Cipher cipher;
        try {
            key = getKey();
            cipher = Cipher.getInstance(_cipher_spec);
            cipher.init(Cipher.ENCRYPT_MODE, key);
            byte[] encVal = cipher.doFinal(clearText.getBytes());
            String encryptedValue = Base64.getEncoder().encodeToString(encVal);
            return encryptedValue;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String convertToEntityAttribute(String encryptedText) {
        Key key;
        try {
            key = getKey();
            Cipher cipher = Cipher.getInstance(_cipher_spec);
            cipher.init(Cipher.DECRYPT_MODE, key);
            byte[] decordedValue = Base64.getDecoder().decode(encryptedText);
            byte[] decValue = cipher.doFinal(decordedValue);
            String decryptedValue = new String(decValue);
            return decryptedValue;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static Key getKey() throws Exception {
        SecretKeyFactory factory = SecretKeyFactory.getInstance(_keygen_spec);
        KeySpec spec = new PBEKeySpec(_password.toCharArray(), _salt.getBytes(), 65536, 128);
        SecretKey tmp = factory.generateSecret(spec);
        SecretKey secret = new SecretKeySpec(tmp.getEncoded(), _algorithm);
        return secret;
    }
}
