import java.security.GeneralSecurityException;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

/**
 * A PlayActivity variant that plays an encrypted video.
 */
public class PlayActivity2 extends APlayActivity {

    @Override
    public Cipher getCipher() throws GeneralSecurityException {
        final Cipher c = Cipher.getInstance("ARC4");    // NoSuchAlgorithmException, NoSuchPaddingException
        c.init(Cipher.DECRYPT_MODE, new SecretKeySpec("BrianIsInTheKitchen".getBytes(), "ARC4"));   // InvalidKeyException
        return c;
    }

    @Override
    public String getPath() {



        return "THE SELECTED PATH OF THE ENCRYPTED VIDEO";
    }
}
