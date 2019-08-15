import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.StringTokenizer;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;


// TODO: Auto-generated Javadoc

/**
 * The Class SymmetricEncryption.
 */

public class SymmetricEncryption {

    public static void main(String args[]) {

        String signatureKey = "185-188-32-81-185-2-188-103-248-127-38-173-109-200-56-32-81-47-234-4-191-157-26-247";
        String serverTime = "2011-01-12 18:48:43.000";
        String encryptedSignatureKey = "240-230-243-218-251-103-145-3-156-109-41-25-127-185-149-150-36-96-176-154-83-24-20-89";

        SymmetricEncryption sE = new SymmetricEncryption();
        String result1 = sE.genericencrypt(serverTime, signatureKey);

        System.out.println(result1);
    }

    /**
     * The d format.
     */
    public final SimpleDateFormat dFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    /**
     * The ivbytes.
     */
    public byte[] ivbytes = new byte[]{(byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00};

    /**
     * The iv.
     */
    public IvParameterSpec iv = new IvParameterSpec(ivbytes);

    /**
     * Genericencrypt.
     *
     * @param source    the source
     * @param keyString the key string
     * @return the string
     */
    public String genericencrypt(String source, String keyString) {
        try {

            // Generate key
            SecretKey key = getKey(keyString);

            // Create the cipher
            Cipher desCipher = Cipher.getInstance("DESede/CBC/PKCS5Padding");

            // Initialize the cipher for encryption
            desCipher.init(Cipher.ENCRYPT_MODE, key, iv);

            // Our cleartext as bytes
            byte[] cleartext = source.getBytes();

            System.out.println("Server Time ASCII " + new String(cleartext));

            // Encrypt the cleartext
            byte[] ciphertext = desCipher.doFinal(cleartext);

            System.out.println("ciphertext ASCII " + new String(ciphertext));

            // Return a String representation of the cipher text
            return getString(ciphertext);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Gets the key.
     *
     * @param keyString the key string
     * @return the key
     */
    private SecretKey getKey(String keyString) {
        try {
            byte[] bytes = getBytes(keyString);
            return new SecretKeySpec(bytes, "DESede");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Gets the string.
     *
     * @param bytes the bytes
     * @return the string
     */
    public String getString(byte[] bytes) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < bytes.length; i++) {
            byte b = bytes[i];
            sb.append((int) (0x00FF & b));
            if (i + 1 < bytes.length) {
                sb.append("-");
            }
        }
        return sb.toString();
    }

    /**
     * Gets the bytes.
     *
     * @param str the str
     * @return the bytes
     */
    public byte[] getBytes(String str) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        StringTokenizer st = new StringTokenizer(str, "-", false);
        while (st.hasMoreTokens()) {
            int i = Integer.parseInt(st.nextToken());
            bos.write((byte) i);
        }
        return bos.toByteArray();
    }
}
