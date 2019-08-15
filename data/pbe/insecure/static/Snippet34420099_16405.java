import org.apache.commons.io.IOUtils;

public class CryptoTest extends TestCase {

    public void testEncryption() throws Exception {

        String DEFAULT_ALG = "AES/ECB/PKCS5Padding";
        String DEFAULT_SALT = "SALT";
        int DEFAULT_ITERATIONS = 10000;
        int DEFAULT_KEY_LEN = 128;

        String alg = DEFAULT_ALG;
        String salt = DEFAULT_SALT;
        int iterations = DEFAULT_ITERATIONS;
        int keyLen = DEFAULT_KEY_LEN;

        SecretKeyFactory factory = null;
        String passPhrase = "password";
        String algOnly = alg.split("/")[0];
        String password = "CDE#VFR$";

        try {
            factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        } catch (NoSuchAlgorithmException e) {
            throw new IOException("Can't load SecretKeyFactory", e);
        }

        SecretKeySpec key = null;
        try {
            key = new SecretKeySpec(
                    factory.generateSecret(
                            new PBEKeySpec(passPhrase.toCharArray(), salt.getBytes(), iterations, keyLen)).getEncoded(),
                    algOnly);
        } catch (Exception e) {
            throw new IOException("Can't generate secret key", e);
        }

        Cipher crypto = null;

        try {
            crypto = Cipher.getInstance(alg);
        } catch (Exception e) {
            throw new IOException("Can't initialize the decryptor", e);
        }

        byte[] encryptedBytes;

        try {
            crypto.init(Cipher.ENCRYPT_MODE, key);
            encryptedBytes = crypto.doFinal(password.getBytes());

            OutputStream os = new FileOutputStream("encrypted.txt");
            IOUtils.write(encryptedBytes, os);

        } catch (Exception e) {
            throw new IOException("Can't decrypt the password", e);
        }
    }
}
