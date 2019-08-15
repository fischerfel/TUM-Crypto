import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class Encryption
{
    public static void main(String args[]) throws Exception {
        System.out.println(encrypt());
    }

    public static String encrypt() throws Exception {
        try {
            String data = "Test string";
            String key = "1234567812345678";
            String iv = "1234567812345678";

            Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
            int blockSize = cipher.getBlockSize();

            // We need to pad with zeros to a multiple of the cipher block size,
            // so first figure out what the size of the plaintext needs to be.
            byte[] dataBytes = data.getBytes();
            int plaintextLength = dataBytes.length;
            int remainder = plaintextLength % blockSize;
            if (remainder != 0) {
                plaintextLength += (blockSize - remainder);
            }

            // In java, primitive arrays of integer types have all elements
            // initialized to zero, so no need to explicitly zero any part of
            // the array.
            byte[] plaintext = new byte[plaintextLength];

            // Copy our actual data into the beginning of the array.  The
            // rest of the array is implicitly zero-filled, as desired.
            System.arraycopy(dataBytes, 0, plaintext, 0, dataBytes.length);

            SecretKeySpec keyspec = new SecretKeySpec(key.getBytes(), "AES");
            IvParameterSpec ivspec = new IvParameterSpec(iv.getBytes());

            cipher.init(Cipher.ENCRYPT_MODE, keyspec, ivspec);
            byte[] encrypted = cipher.doFinal(plaintext);

            return new sun.misc.BASE64Encoder().encode(encrypted);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
