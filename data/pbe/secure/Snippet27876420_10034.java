public class AES256EncryptionServiceBean implements EncryptionService {

    private static final Logger LOGGER = LoggerFactory
            .getLogger(AES256EncryptionServiceBean.class);
    private String salt = null; //get bytes out of UTF-8 for decryption
    private static final int PSWDITERATIONS = 1000;//65536;
    private static final int KEYSIZE = 256;
    private static final String AES_ALGO = "AES";
    private static final String SHA1_ALGO = "PBKDF2WithHmacSHA1";
    private static final String AES_CBC_PKCS5_TRANSFORM = "AES/CBC/PKCS5Padding";
    private byte[] Iv;

    /**
     * Encrypts the data with AES-256 algorithm Encrypted data will be encoded
     * with base64 algorithm and the returned. Initial vector is being used
     * during encryption along with CBC encryption mode.
     * 
     * output format: [algo indicator(1char)][Initialization vector()][salt()][encoded data(variable size)]
     */
    @Override
    public byte[] encrypt(String password, byte[] data) throws PibException {
        byte[] encodedData = null;
        try {
            byte[] encryptedData = encryptCBC256Bits(password, data);
            encodedData = Base64.encodeBase64(encryptedData);
            /*String finalStr=null;
            String algo256 = "2";
            String datastr = Base64.encodeBase64String(encryptedData);
            String ivstr = new String(Iv);
            finalStr = algo256 +ivstr+salt+datastr;

            encodedData = finalStr.getBytes();
             */
        } catch (Exception e) {
            throw ExceptionFactory.createPibException(
                    MessageCodes.PIB_ENCRYPTION_FAILED, e, LOGGER);
        }
        return encodedData;
    }

    /**
     * Encrypts the input data with AES CBC transformation using 256 bits (32
     * bytes) Key is generated based on the provided password and random salt.
     * Salt is the extra bits added to the password to ensure every key is
     * unique SHA1 hashing is also participate in key generation.
     * 
     * @throws PibException
     * 
     */
    private byte[] encryptCBC256Bits(String password, byte[] data)
            throws PibException {

        salt = generateSalt();
        byte[] saltBytes = salt.getBytes(StandardCharsets.UTF_8);
        byte[] encryptedTextBytes = null;

        // Derive the key

        try {
            SecretKeyFactory factory = SecretKeyFactory.getInstance(SHA1_ALGO);
            // Password based key specification
            PBEKeySpec spec = new PBEKeySpec(password.toCharArray(), saltBytes,
                    PSWDITERATIONS, KEYSIZE);
            SecretKey secretKey = factory.generateSecret(spec);
            SecretKeySpec secret = new SecretKeySpec(secretKey.getEncoded(),
                    AES_ALGO);

            // encrypt the data
            Cipher cipher = Cipher.getInstance(AES_CBC_PKCS5_TRANSFORM);
            // SecureRandom random = new SecureRandom();
            // byte[] ivTemp = new byte[16];
            // random.nextBytes(ivTemp);
            cipher.init(Cipher.ENCRYPT_MODE, secret);
            AlgorithmParameters params = cipher.getParameters();
            Iv = params.getParameterSpec(IvParameterSpec.class).getIV();
            encryptedTextBytes = cipher.doFinal(data);

        } catch (NoSuchAlgorithmException | InvalidKeySpecException
                | NoSuchPaddingException | InvalidKeyException
                | InvalidParameterSpecException | IllegalBlockSizeException
                | BadPaddingException e) {
            throw ExceptionFactory.createPibException(
                    MessageCodes.PIB_ENCRYPTION_FAILED, e, LOGGER);
        }

        return encryptedTextBytes;
    }

    private String generateSalt() {

        SecureRandom random = new SecureRandom();
        byte bytes[] = new byte[20];
        random.nextBytes(bytes);
        String s = new String(bytes);
        return s;

    }

    public String getSalt() {
        return salt;
    }

    public byte[] getIv() {
        return Iv;
    }

}
