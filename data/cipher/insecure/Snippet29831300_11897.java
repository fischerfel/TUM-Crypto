1. Stored the private key in hex string. I.e. 48 chars hex string as below which is equivalent to 24 bytes reuquired for 3des
73AD9CEC99816AA6A4D82FB273AD9CEC99816AA6A4D82FB2
2. Following is code written in java 
https://github.com/dilipkumar2k6/3des/blob/master/TripleDes.java
import java.security.GeneralSecurityException;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;


public class TripleDes {
    // Crypto library related keys
    private static final String ALGO_NAME = "DESede/ECB/NoPadding";
    private static final int PADDING_BLOCK = 8;

    // Test Data
    private static final String PLAIN_TEXT = "Hello World";
    private static final String SHARED_KEY = "73AD9CEC99816AA6A4D82FB273AD9CEC99816AA6A4D82FB2";

    public static void main(String[] arg) {

        try {
            // Get Algorithm name
            String desAlgoName = getDESAlgorithmName(ALGO_NAME);
            // Create Cipher object
            Cipher cipher = Cipher.getInstance(ALGO_NAME);
            //Actual DES algo needs 56 bits key, which is equivalent to 1byte (0 at 0th position)  Get 8*3 byets key
            byte [] key = hexFromString(SHARED_KEY);
            System.out.println("DES Algorithm  shared key size in bytes >> "+key.length);
            // Create SecretKeySpec
            SecretKeySpec secretKeySpec = new SecretKeySpec(key, desAlgoName);
            //Encrypt bytes
            byte [] encryptedBytes = encryptIntoBytes(cipher, secretKeySpec, PLAIN_TEXT.getBytes(), 0, PLAIN_TEXT.getBytes().length);
            String encryptedString=  hexToString(encryptedBytes);
            System.out.println(encryptedString);

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static byte[] encryptIntoBytes(Cipher cipher, SecretKeySpec secretKeySpec, byte[] dct, int offset, int len) throws GeneralSecurityException {
        cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
        byte[] ect = cipher.doFinal(addPadding(dct, offset, len));
        return ect;
    }

    public static String getDESAlgorithmName(String algoName) {
        System.out.println("getDESAlgorithmName algoName >> "+algoName);
        String desAlgoName = null;
        int i = algoName.indexOf("/");
        if (i != -1)
            desAlgoName = algoName.substring(0, i);
        else
            desAlgoName = algoName;
        return desAlgoName;
    }

    /**
     * Adds padding characters to the data to be encrypted. Also adds random
     * Initial Value to the beginning of the encrypted data when using Triple
     * DES in CBC mode (DES-EDE3/CBC).
     * 
     * @param inData
     *            Array of bytes to be padded
     * @param offset
     *            Offset to starting point within array
     * @param len
     *            Number of bytes to be encrypted
     * @return Padded array of bytes
     */
    public static byte[] addPadding(byte[] inData, int offset, int len) {
        System.out.println("addPadding offset >> "+offset+", len >> "+len);
        byte[] bp = null;
        int padChars = PADDING_BLOCK; // start with max padding value
        int partial = (len + 1) % padChars; // calculate how many extra bytes
                                            // exist
        if (partial == 0) {
            padChars = 1; // if none, set to only pad with length byte
        } else {
            padChars = padChars - partial + 1; // calculate padding size to
                                                // include length
        }
        System.out.println("addPadding >> Add padding of "+padChars);
        /*
         * Create a byte array large enough to hold data plus padding bytes The
         * count of padding bytes is placed in the first byte of the data to be
         * encrypted. That byte is included in the count.
         */
        bp = new byte[len + padChars];
        bp[0] = Byte.parseByte(Integer.toString(padChars));
        System.arraycopy(inData, offset, bp, 1, len);
        return bp;
    }

    public static byte[] hexFromString(String hex) {
        int len = hex.length();
        byte[] buf = new byte[((len + 1) / 2)];

        int i = 0, j = 0;
        if ((len % 2) == 1)
            buf[j++] = (byte) fromDigit(hex.charAt(i++));

        while (i < len) {
            buf[j++] = (byte) ((fromDigit(hex.charAt(i++)) << 4) | fromDigit(hex
                    .charAt(i++)));
        }
        return buf;
    }

    public static int fromDigit(char ch) {
        if (ch >= '0' && ch <= '9')
            return ch - '0';
        if (ch >= 'A' && ch <= 'F')
            return ch - 'A' + 10;
        if (ch >= 'a' && ch <= 'f')
            return ch - 'a' + 10;

        throw new IllegalArgumentException("invalid hex digit '" + ch + "'");
    }

    public static String hexToString(byte[] ba) {
        return hexToString(ba, 0, ba.length);
    }

    public static final char[] hexDigits = { '0', '1', '2', '3', '4', '5',
            '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };

    public static String hexToString(byte[] ba, int offset, int length) {
        char[] buf = new char[length * 2];
        int j = 0;
        int k;

        for (int i = offset; i < offset + length; i++) {
            k = ba[i];
            buf[j++] = hexDigits[(k >>> 4) & 0x0F];
            buf[j++] = hexDigits[k & 0x0F];
        }
        return new String(buf);
    }

}
