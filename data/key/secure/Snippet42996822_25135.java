import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 * Class to calculate CMAC which is used as PRF in KDF for SCP03 PseudoRandom CardChallenge generation
 */
public class Cmac {

    /**
     * Tested CMAC against official TestVectors ({@link 'http://csrc.nist.gov/publications/nistpubs/800-38B/SP_800-38B.pdf'})
     * CMAC used as PRF in KDF
     */

    public static byte[] calc(byte[] keyBytes, byte[] data) throws ApduGeneratorException {
        try {
            SecretKey key = new SecretKeySpec(keyBytes, 0, keyBytes.length, "AES");
            Mac mac = Mac.getInstance("CmacAES", BouncyCastleProvider.PROVIDER_NAME);
            mac.init(key);

            byte[] hash = mac.doFinal(data);

            return hash;
        } catch (Exception e) {
            throw new ApduGeneratorException(e.getMessage());
        }
    }
}
