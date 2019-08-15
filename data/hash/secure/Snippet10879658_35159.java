import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 
 * Implementation of Concatenation Key Derivation Function<br/>
 * http://csrc.nist.gov/publications/nistpubs/800-56A/SP800-56A_Revision1_Mar08-2007.pdf
 *
 */

public class ConcatKeyDerivationFunction {

    private static final long MAX_HASH_INPUTLEN = Long.MAX_VALUE;
    private static final long UNSIGNED_INT_MAX_VALUE = 4294967295L;
    private static MessageDigest md;

    public ConcatKeyDerivationFunction(String hashAlg) throws NoSuchAlgorithmException {
        md = MessageDigest.getInstance(hashAlg);
    }

    public byte[] concatKDF(byte[] z, int keyDataLen, byte[] algorithmID, byte[] partyUInfo, byte[] partyVInfo, byte[] suppPubInfo, byte[] suppPrivInfo) {
        int hashLen = md.getDigestLength() * 8;

        if (keyDataLen % 8 != 0) {
            throw new IllegalArgumentException("keydatalen should be a multiple of 8");
        }

        if (keyDataLen > (long) hashLen * UNSIGNED_INT_MAX_VALUE) {
            throw new IllegalArgumentException("keydatalen is too large");
        }

        if (algorithmID == null || partyUInfo == null || partyVInfo == null) {
            throw new NullPointerException("Required parameter is null");
        }

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            baos.write(algorithmID);
            baos.write(partyUInfo);
            baos.write(partyVInfo);
            if (suppPubInfo != null) {
                baos.write(suppPubInfo);
            }
            if (suppPrivInfo != null) {
                baos.write(suppPrivInfo);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        byte[] otherInfo = baos.toByteArray();
        return concatKDF(z, keyDataLen, otherInfo);
    }

    private byte[] concatKDF(byte[] z, int keyDataLen, byte[] otherInfo) {
        keyDataLen = keyDataLen / 8;
        byte[] key = new byte[keyDataLen];

        int hashLen = md.getDigestLength();
        int reps = keyDataLen / hashLen;

        if (reps > UNSIGNED_INT_MAX_VALUE) {
            throw new IllegalArgumentException("Key derivation failed");
        }

        int counter = 1;
        byte[] counterInBytes = intToFourBytes(counter);

        if ((counterInBytes.length + z.length + otherInfo.length) * 8 > MAX_HASH_INPUTLEN) {
            throw new IllegalArgumentException("Key derivation failed");
        }

        for (int i = 0; i <= reps; i++) {
            md.reset();
            md.update(intToFourBytes(i + 1));
            md.update(z);
            md.update(otherInfo);

            byte[] hash = md.digest();
            if (i < reps) {
                System.arraycopy(hash, 0, key, hashLen * i, hashLen);
            } else {
                System.arraycopy(hash, 0, key, hashLen * i, keyDataLen % hashLen);
            }
        }
        return key;
    }

    private byte[] intToFourBytes(int i) {
        byte[] res = new byte[4];
        res[0] = (byte) (i >>> 24);
        res[1] = (byte) ((i >>> 16) & 0xFF);
        res[2] = (byte) ((i >>> 8) & 0xFF);
        res[3] = (byte) (i & 0xFF);
        return res;
    }
}
