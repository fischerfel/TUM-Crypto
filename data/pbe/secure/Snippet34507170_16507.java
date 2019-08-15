public class AesEncryption {
    private static final int KEY_SIZE = 16;
    private static final int OUTPUT_KEY_LENGTH = 256;
    private static final int ITERATIONS = 1000;

    private String mPassphraseOrPin;

    public AesEncryption(String passphraseOrPin) {
        mPassphraseOrPin = passphraseOrPin;
    }

    public void encrypt(String id, String textToEncrypt) throws Exception {
        byte[] iv = getIv();
        SecretKey secretKey = generateKey(iv);

        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, new IvParameterSpec(iv));

        byte[] cipherText = cipher.doFinal(textToEncrypt.getBytes("utf-8"));
        byte[] ivCipherText = arrayConcat(iv, cipherText);
        String encryptedText = Base64.encodeToString(ivCipherText, Base64.NO_WRAP);

        storeEncryptedTextInDb(id, encryptedText);
    }

    public String decrypt(String id) throws Exception {
        String encryptedText = getEncryptedTextFromDb(id);

        byte[] ivCipherText = Base64.decode(encryptedText, Base64.NO_WRAP);
        byte[] iv = Arrays.copyOfRange(ivCipherText, 0, KEY_SIZE);
        byte[] cipherText = Arrays.copyOfRange(ivCipherText, KEY_SIZE, ivCipherText.length);

        SecretKey secretKey = generateKey(iv);

        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, secretKey, new IvParameterSpec(iv));
        String decrypted = new String(cipher.doFinal(cipherText), "utf-8");

        return decrypted;
    }

    public SecretKey generateKey(byte[] salt) throws Exception {
        SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        KeySpec keySpec = new PBEKeySpec(mPassphraseOrPin.toCharArray(), salt, ITERATIONS, OUTPUT_KEY_LENGTH);
        SecretKey tmp = secretKeyFactory.generateSecret(keySpec);
        return new SecretKeySpec(tmp.getEncoded(), "AES");
    }

    private byte[] getIv() {
        byte[] salt = new byte[KEY_SIZE];
        new SecureRandom().nextBytes(salt);
        return salt;
    }

    private byte[] arrayConcat(byte[] one, byte[] two) {
        byte[] combined = new byte[one.length + two.length];
        for (int i = 0; i < combined.length; ++i) {
            combined[i] = i < one.length ? one[i] : two[i - one.length];
        }
        return combined;
    }
}
