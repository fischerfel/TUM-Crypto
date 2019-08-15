    public class SunJceCryptoService implements CryptoService {

    private static final int KEY_DERIVATION_ITERATION = 5_000_000;
    private static final int KEY_LENGTH = 256;

    private static final String PROVIDER_NAME = "SunJCE";
    private static final String KEY_DERIVATION_ALGORITHM = "PBKDF2WithHmacSHA256";
    private static final String SECURE_RANDOM_ALGORITHM = "SHA1PRNG";
    private static final String CIPHER_ALGORITHM = "AES";
    private static final String CIPHER_MODE = "AES_256/GCM/NoPadding";
    private static final String PAYLOAD_SEPARATOR = " ";

    private final String password;

    public SunJceCryptoService(String password) {
        this.password = password;
    }

    public String encrypt(final String clearText) {

        try {
            Cipher cipher = Cipher.getInstance(CIPHER_MODE, PROVIDER_NAME);
            final byte[] salt = generateSalt();
            final byte[] iv = new byte[12];
            SecureRandom.getInstance(SECURE_RANDOM_ALGORITHM).nextBytes(iv);
            GCMParameterSpec gcmParameterSpec = new GCMParameterSpec(128, iv);
            cipher.init(Cipher.ENCRYPT_MODE, deriveSecretKey(password, salt), gcmParameterSpec);

            final byte[] aad = new byte[12];
            SecureRandom.getInstance(SECURE_RANDOM_ALGORITHM).nextBytes(aad);
            cipher.updateAAD(aad);
            byte[] encryptedBytes = cipher.doFinal(clearText.getBytes(StandardCharsets.UTF_8));

            return new String(Base64.getEncoder().encode(iv), StandardCharsets.UTF_8) + PAYLOAD_SEPARATOR +
                    new String(Base64.getEncoder().encode(aad), StandardCharsets.UTF_8) + PAYLOAD_SEPARATOR +
                    new String(Base64.getEncoder().encode(encryptedBytes), StandardCharsets.UTF_8) + PAYLOAD_SEPARATOR +
                    new String(Base64.getEncoder().encode(salt), StandardCharsets.UTF_8);

        } catch (Exception e) {
            throw new CryptoServiceException();
        }
    }

    public String decrypt(final String encryptedPayload) {

        try {
            final Cipher cipher = Cipher.getInstance(CIPHER_MODE, PROVIDER_NAME);
            final String[] sections = encryptedPayload.split(PAYLOAD_SEPARATOR);
            final byte[] iv = Base64.getDecoder().decode(sections[0]);
            final byte[] aad = Base64.getDecoder().decode(sections[1]);
            final byte[] encryptedText = Base64.getDecoder().decode(sections[2]);
            final byte[] salt = Base64.getDecoder().decode(sections[3]);

            GCMParameterSpec gcmParameterSpec = new GCMParameterSpec(128, iv);
            cipher.init(Cipher.DECRYPT_MODE, deriveSecretKey(password, salt), gcmParameterSpec);
            cipher.updateAAD(aad);

            return new String(cipher.doFinal(encryptedText));
        } catch (Exception e) {
            throw new CryptoServiceException();
        }
    }

    public SecretKey deriveSecretKey(String password, byte[] salt) {
        SecretKeyFactory secretKeyFactory;
        try {
            secretKeyFactory = SecretKeyFactory.getInstance(KEY_DERIVATION_ALGORITHM);

            return new SecretKeySpec(secretKeyFactory
                    .generateSecret(
                            new PBEKeySpec(password.toCharArray(),
                                    salt,
                                    KEY_DERIVATION_ITERATION,
                                    KEY_LENGTH)
                    ).getEncoded(),
                    CIPHER_ALGORITHM);
        } catch (Exception e) {
            throw new CryptoServiceException();
        }
    }

    /**
     * ideally, the salt value is a random (or pseudorandom) string of the length HashLen
     */
    public byte[] generateSalt() {
        try {
            byte salt[] = new byte[KEY_LENGTH / 8];
            SecureRandom secureRandom = SecureRandom.getInstance(SECURE_RANDOM_ALGORITHM);
            //SecureRandom secureRandom = SecureRandom.getInstanceStrong();
            secureRandom.nextBytes(salt);
            return salt;
        } catch (Exception e) {
            throw new CryptoServiceException();
        }
    }
    }
