import java.security.GeneralSecurityException;
import java.security.Key;
import java.security.Security;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import android.util.Base64;

public class AESTest {

    public enum Mode {
        CBC, ECB, CFB, OFB, PCBC
    };

    public enum Padding {
        NoPadding, PKCS5Padding, PKCS7Padding, ISO10126d2Padding, X932Padding, ISO7816d4Padding, ZeroBytePadding
    }

    private static final String ALGORITHM = "AES";

    private static final byte[] keyValue ="myKey".getBytes();


    String decrypt(String valueToDec, Mode modeOption,
            Padding paddingOption) throws GeneralSecurityException {



        byte[] decodeBase64 = Base64.decode(valueToDec.getBytes(),0);

        Key key = new SecretKeySpec(keyValue, ALGORITHM); 
        Cipher c = Cipher.getInstance("AES/ECB/NoPadding"); 
        c.init(Cipher.DECRYPT_MODE, key); 
        byte[] encValue = c.doFinal(decodeBase64); 
        return new String(encValue).trim();

    }

}
