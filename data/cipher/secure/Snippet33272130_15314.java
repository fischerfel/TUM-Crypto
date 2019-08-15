import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;

import **.CryptoService;
import **.CryptoServiceException;


public class CryptoServiceImpl implements CryptoService {

public byte[] encrypt(byte[] data, String key) throws Exception {
        try {
            Cipher cipher = Cipher.getInstance("RSA");
            Base64 decoder = new Base64(64);

            // decode the base64 encoded string
            byte[] decodedKey = decoder.decode(key);
            // rebuild key using SecretKeySpec
            final SecretKeySpec originalKey = new SecretKeySpec(decodedKey, 0, decodedKey.length, "RSA");

            cipher.init(Cipher.ENCRYPT_MODE, originalKey);
            final String encryptedString = Base64.encodeBase64String(cipher.doFinal(data));
            return encryptedString.getBytes();
        } catch (Exception e) {
            throw new CryptoServiceException("Cannot encrypt data using key '", e);
        }

    }

    public byte[] decrypt(byte[] data, String key) throws Exception {
        try {
            Cipher cipher = Cipher.getInstance("RSA");
            Base64 decoder = new Base64(64);

            // decode the base64 encoded string
            byte[] decodedKey = decoder.decode(key);
            // rebuild key using SecretKeySpec
            final SecretKeySpec originalKey = new SecretKeySpec(decodedKey, 0, decodedKey.length, "RSA");

            cipher.init(Cipher.DECRYPT_MODE, originalKey);
            final String decryptedString = new String(cipher.doFinal(Base64.decodeBase64(new String(data))));
            return decryptedString.getBytes();
        } catch (Exception e) {
            throw new CryptoServiceException("Cannot decrypt data using key '", e);

        }
    }
}
