public class SymmetricCipherTest {
    private static final String DEFAULT_ENCRYPTION_ALGORITHM = "PBEWithMD5AndTripleDES";
    public final String ENCODE_INDICATOR_START = "ENC(";
    public final String ENCODE_INDICATOR_END = ")";
    public final String APP_ENCRYPTION_KEY_FILE = "application/.encryption.key";
    public static final int INTERATION = 15;
    private static final byte[] SALT = { (byte) 0xd7, (byte) 0x73, (byte) 0x21, (byte) 0x8c, (byte) 0x7e, (byte) 0xc8, (byte) 0xee,
            (byte) 0x99 };

//  private static SymmetricCipherTest instance = initApplicaitonKey();
    private static Base64 base64 = new Base64();
    private static Cipher encrypter;
    private static Cipher decrypter;
//  private final Base64 base64 = new Base64();

    public final String ERROR_KEY_GENERATION = "Encryption key generation failed. Please verify the logs.";
    public static void main(String[] args) throws InvalidKeySpecException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException, DecoderException, NoSuchAlgorithmException {
        String applicationKey="abcdefghijklmnopqrstu";
        String password="HellowWorld";
            encrypter=getCipherObject(applicationKey);
            String encriptedString=new String(base64.encode(encrypter.doFinal(password.getBytes())));
            System.out.println(encriptedString);

            decrypter=getCipherObject(applicationKey);
             String decryiptedString=new String(decrypter.doFinal(base64.decode(encriptedString.getBytes())));
            System.out.println(decryiptedString);

    }

    public static Cipher getCipherObject(String applicationKey) throws NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException{
        SecretKeyFactory kf = SecretKeyFactory.getInstance(DEFAULT_ENCRYPTION_ALGORITHM);
        PBEKeySpec keySpec = new PBEKeySpec(applicationKey.toCharArray());
        SecretKey key = kf.generateSecret(keySpec);
        Cipher ciph = Cipher.getInstance(DEFAULT_ENCRYPTION_ALGORITHM);

        PBEParameterSpec params = new PBEParameterSpec(SALT, INTERATION);
        ciph.init(Cipher.ENCRYPT_MODE, key, params);
        return ciph;
    }
}
