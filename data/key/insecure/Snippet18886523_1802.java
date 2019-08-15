 // Cryptographic algorithm
  private final String ALGORITHM = "AES";
  //
 private final byte[] ENCRYPTION_KEY = new byte[]
 {
        'E', 'r', ';', '|', '<', '@', 'p', 'p', 'l', '1', 'c', '@', 't', '1', '0', 'n'
 };

public byte[] encryptValue(byte[] valueToEnc) throws EncryptionException
{
    try
    {
        // Constructs a secret key from the given byte array and algorithm
        Key key = new SecretKeySpec(ENCRYPTION_KEY, ALGORITHM);
        // Creating Cipher object by calling getInstance() factory methods and
        // passing ALGORITHIM VALUE = "AES" which is a 128-bit block cipher
        // supporting keys of 128, 192, and 256 bits.
        Cipher c = Cipher.getInstance(ALGORITHM);
        // Initialize a Cipher object with Encryption Mode and generated key
        c.init(Cipher.ENCRYPT_MODE, key);
        // To encrypt data in a single step, calling doFinal() methods: If we
        // want to encrypt data in multiple steps, then need to call update()
        // methods instead of doFinal()
        byte[] encValue = c.doFinal(valueToEnc);
        // Encrypting value using Apache Base64().encode method
        byte[] encryptedByteValue = new Base64().encode(encValue);

        return encryptedByteValue;
    }
    catch (Exception e)
    {
        throw new EncryptionException(e.getMessage(), e);
    }
}
