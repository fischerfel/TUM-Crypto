/**
 *
 * @param <T>
 *            source
 * @param <V>
 *            result
 * @param <K>
 *            key
 */
public interface BaseDecryption<S, R, K> {

        public static enum DecryType {
            AES128CBC, AES128XOR, XOR
        }

        public BaseDecryption<S, R, K> withDecryType(DecryType type);

        public DecryType getDecryType();

        public R decrypt(S source);

    }


public abstract class BytesDecryption implements
            BaseDecryption<byte[], byte[], byte[]> {

        private DecryType decrypTye;

        /**
         * Here is where I used the treadLocal
         * 
         */
        private ThreadLocal<byte[]> key = new ThreadLocal<byte[]>();

        protected DecryType getDecrypTye() {
            return decrypTye;
        }

        protected byte[] getKey() {
            return this.key.get();
        }

        public BaseDecryption<byte[], byte[], byte[]> withDecryKey(byte[] key) {
            this.key.set(key);
            return this;
        }

        @Override
        public BaseDecryption<byte[], byte[], byte[]> withDecryType(
                DecryType decryType) {
            this.decrypTye = decryType;
            return this;
        }

    }

@Component("LEAD_AES128CBC")
public class AES128CBC extends BytesDecryption {

    private AlgorithmParameters params;
    private static final String KEY_ALGORITHM = "AES";
    public static final String CIPHER_ALGORITHM = "AES/CBC/PKCS7Padding";
    private static final Logger logger = LoggerFactory
            .getLogger(AES128CBC.class);

    public AES128CBC() throws NoSuchAlgorithmException,
            InvalidParameterSpecException {
        Security.addProvider(new BouncyCastleProvider());
        this.withDecryType(DecryType.AES128CBC);
        initVi();
    }

    @Override
    public byte[] decrypt(byte[] source) {
        byte[] key = getKey();
        byte[] size16Key = new byte[16];
        System.arraycopy(key, 0, size16Key, 0, 16);
        SecretKey secretKey = new SecretKeySpec(size16Key, KEY_ALGORITHM);
        try {
            Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, secretKey, params);
            if (source.length % 16 != 0) {
                byte[] encryptionBytes = new byte[source.length - source.length
                        % 16];
                System.arraycopy(source, 0, encryptionBytes, 0,
                        encryptionBytes.length);
                byte[] decryptionBytes = cipher.doFinal(encryptionBytes);
                byte[] finalBytes = new byte[decryptionBytes.length
                        + source.length % 16];
                System.arraycopy(decryptionBytes, 0, finalBytes, 0, 0);
                // only multiple of 16 bytes will be decrypted, so copy the
                // remained
                System.arraycopy(source, encryptionBytes.length, finalBytes,
                        encryptionBytes.length, source.length % 16);
                return finalBytes;
            }
            return cipher.doFinal(source);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException
                | IllegalBlockSizeException | BadPaddingException
                | InvalidKeyException | InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public DecryType getDecryType() {
        return DecryType.AES128CBC;
    }

    public void initVi() throws NoSuchAlgorithmException,
            InvalidParameterSpecException {
        byte[] iv = new byte[16];
        Arrays.fill(iv, (byte) 0x00);
        params = AlgorithmParameters.getInstance(KEY_ALGORITHM);
        params.init(new IvParameterSpec(iv));
    }

    public static byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                                 + Character.digit(s.charAt(i+1), 16));
        }
        return data;
    }
}
