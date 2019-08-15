public class TripleDES {

public static int MAX_KEY_LENGTH = DESedeKeySpec.DES_EDE_KEY_LEN;
private static String ENCRYPTION_KEY_TYPE = "DESede";
private static String ENCRYPTION_ALGORITHM = "DESede/ECB/PKCS7Padding";
private final SecretKeySpec keySpec;
private final static String LOG = "TripleDES";

public TripleDES(String passphrase) {
    byte[] key;
    try {
        // get bytes representation of the password
        key = passphrase.getBytes("UTF8");
    } catch (UnsupportedEncodingException e) {
        throw new IllegalArgumentException(e);
    }

    key = padKeyToLength(key, MAX_KEY_LENGTH);
    key = addParity(key);
    keySpec = new SecretKeySpec(key, ENCRYPTION_KEY_TYPE);
}

// !!! - see post below
private byte[] padKeyToLength(byte[] key, int len) {
    byte[] newKey = new byte[len];
    System.arraycopy(key, 0, newKey, 0, Math.min(key.length, len));
    return newKey;
}

// standard stuff
public byte[] encrypt(String message) throws GeneralSecurityException, UnsupportedEncodingException {
    byte[] unencrypted = message.getBytes("UTF8");
    return doCipher(unencrypted, Cipher.ENCRYPT_MODE);
}

public byte[] decrypt(byte[] encrypted) throws GeneralSecurityException {
    return doCipher(encrypted, Cipher.DECRYPT_MODE);
}

private byte[] doCipher(byte[] original, int mode)
        throws GeneralSecurityException {
    Cipher cipher = Cipher.getInstance(ENCRYPTION_ALGORITHM);
    // IV = 0 is yet another issue, we'll ignore it here
    // IvParameterSpec iv = new IvParameterSpec(new byte[] { 0, 0, 0, 0, 0, 0, 0, 0 });
    cipher.init(mode, keySpec); //, iv);
    return cipher.doFinal(original);
}

// Takes a 7-byte quantity and returns a valid 8-byte DES key.
// The input and output bytes are big-endian, where the most significant
// byte is in element 0.
public static byte[] addParity(byte[] in) {
    byte[] result = new byte[8];

    // Keeps track of the bit position in the result
    int resultIx = 1;

    // Used to keep track of the number of 1 bits in each 7-bit chunk
    int bitCount = 0;

    // Process each of the 56 bits
    for (int i = 0; i < 56; i++) {
        // Get the bit at bit position i
        boolean bit = (in[6 - i / 8] & (1 << (i % 8))) > 0;

        // If set, set the corresponding bit in the result
        if (bit) {
            result[7 - resultIx / 8] |= (1 << (resultIx % 8)) & 0xFF;
            bitCount++;
        }

        // Set the parity bit after every 7 bits
        if ((i + 1) % 7 == 0) {
            if (bitCount % 2 == 0) {
                // Set low-order bit (parity bit) if bit count is even
                result[7 - resultIx / 8] |= 1;
            }
            resultIx++;
            bitCount = 0;
        }
        resultIx++;
    }

    Log.d(LOG, "result: " + result);
    return result;
}
}
