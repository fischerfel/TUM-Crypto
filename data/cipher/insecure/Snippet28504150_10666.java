import java.security.Key;

import javax.crypto.Cipher; import javax.crypto.spec.SecretKeySpec;
import javax.persistence.AttributeConverter;

import org.postgresql.util.Base64; 
import org.springframework.beans.factory.annotation.Value; 
import org.springframework.context.annotation.Bean; 
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.stereotype.Component;

@javax.persistence.Converter 
@Component 
public class EntityEncryptionConverter implements AttributeConverter<String, String> {

    @Value("${general.key}")
    private String keyCode;

    private static final String ALGORITHM = "AES/ECB/PKCS5Padding";
    private static final byte[] KEY = "395DEADE4D23DD92".getBytes();

    public String convertToDatabaseColumn(String ccNumber) {
        System.out.print(keyCode);
        // do some encryption
        Key key = new SecretKeySpec(KEY, "AES");
        try {
            Cipher c = Cipher.getInstance(ALGORITHM);
            c.init(Cipher.ENCRYPT_MODE, key);
            return Base64.encodeBytes(c.doFinal(ccNumber.getBytes()));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public String convertToEntityAttribute(String dbData) {
        // do some decryption
        Key key = new SecretKeySpec(KEY, "AES");
        try {
            Cipher c = Cipher.getInstance(ALGORITHM);
            c.init(Cipher.DECRYPT_MODE, key);
            return new String(c.doFinal(Base64.decode(dbData)));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
