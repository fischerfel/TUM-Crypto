public class AESEncryption
{
    private final String ALGORITHM = "AES";
    private final String MAC_ALGORITHM = "HmacSHA256";
    private final String PRNG_ALGORITHM = "SHA1PRNG";
    private final String TRANSFORMATION = "AES/CBC/PKCS5Padding";
    private final String PLAINTEXT = "/Volumes/CONNOR P/Unencrypted.txt";
    private final String ENCRYPTED = "/Volumes/CONNOR P/Encrypted.txt";
    private final String PASSWORD = "javapapers";
    private final String IV_FILE_NAME = "iv.enc";
    private final String LOC = Paths.get(".").toAbsolutePath().normalize().toString();
    private final int SALT_SIZE = 16;
    private final int IV_SIZE = 16;
    private final int KEY_LENGTH = 128;
    private final int ITERATIONS = 100000;
    private final int START = 0;
    public AESEncryption()
    {
        try
        {
            encrypt();
        }
        catch(Exception ex)
        {
            JOptionPane.showMessageDialog(null, "Error: " + ex.getClass().getName(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    private void encrypt() throws Exception
    {
        File encrypted = new File(ENCRYPTED);
        File plaintext = new File(PLAINTEXT);
        int plaintextSize = (int)plaintext.length();

        BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream(plaintext));
        BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(encrypted));

        //Create salt for cipher key
        byte[] salt = new byte[SALT_SIZE];
        SecureRandom saltSecureRandom = SecureRandom.getInstance(PRNG_ALGORITHM);
        saltSecureRandom.nextBytes(salt);

        //Create cipher key & use to initialize cipher
        byte[] keyBytes = PBEKeyFactory.getKey(PASSWORD, salt, ITERATIONS, KEY_LENGTH);
        SecretKeySpec secret = new SecretKeySpec(keyBytes, ALGORITHM);

        Cipher cipher = Cipher.getInstance(TRANSFORMATION);
        cipher.init(Cipher.ENCRYPT_MODE, secret, new IvParameterSpec(new byte[16]));

        //Create byte array of encrypted data
        byte[] input = new byte[plaintextSize];
        Path path = Paths.get(PLAINTEXT);
        input = Files.readAllBytes(path);
        byte[] crypt = cipher.doFinal(input);

        //Create salt for the MAC key for added security
        byte[] macsalt = new byte[SALT_SIZE];
        SecureRandom macsaltSecureRandom = SecureRandom.getInstance(PRNG_ALGORITHM);
        macsaltSecureRandom.nextBytes(macsalt);

        //PBEKeyFactory.getKey(password, salt, iterations, keylength)    
        //returns a byte array representation of a SecretKey.
        //Used a SecretKeyFactory instead of a KeyGenerator to make key.
        //SecretKeyFactory gives back the same key given the same specifications
        //whereas KeyGenerator gives back a new random key each time.
        byte[] macPBE = PBEKeyFactory.getKey(PASSWORD, macsalt, ITERATIONS, KEY_LENGTH);

        SecretKeySpec macKey = new SecretKeySpec(macPBE, MAC_ALGORITHM);
        Mac mac = Mac.getInstance(MAC_ALGORITHM);
        mac.init(macKey);
        byte[] macBytes = mac.doFinal(crypt);

        byte[] output = new byte[SALT_SIZE + SALT_SIZE + crypt.length + macBytes.length];
        System.arraycopy(salt, START, output, START, SALT_SIZE);
        System.arraycopy(macsalt, START, output, SALT_SIZE, SALT_SIZE);
        System.arraycopy(macBytes, START, output, SALT_SIZE + SALT_SIZE, macBytes.length);
        System.arraycopy(crypt, START, output, SALT_SIZE + SALT_SIZE + macBytes.length, crypt.length);

        bufferedInputStream.close();   
        bufferedOutputStream.write(output);
        bufferedOutputStream.flush();
        bufferedOutputStream.close();
    }
}

public class AESDecryption
{
    private final String ALGORITHM = "AES";
    private final String MAC_ALGORITHM = "HmacSHA256";
    private final String TRANSFORMATION = "AES/CBC/PKCS5Padding";
    private final String PLAINTEXT = "/Volumes/CONNOR P/De-Encrypted.txt";
    private final String ENCRYPTED = "/Volumes/CONNOR P/Encrypted.txt";
    private final String PASSWORD = "javapapers";
    private final String LOC = Paths.get(".").toAbsolutePath().normalize().toString();
    private final int SALT_SIZE = 16;
    //MAC key size is 256 bits (32 bytes) since it is created with
    //the HmacSHA256 algorithm
    private final int MAC_SIZE = 32;
    private final int IV_SIZE = 16;
    private final int START = 0;
    private final int KEY_LENGTH = 128;
    private final int ITERATIONS = 100000;
    public AESDecryption()
    {
        try
        {
            decrypt();
        }
        catch(Exception ex)
        {
            JOptionPane.showMessageDialog(null, "Error: " + ex.getClass().getName(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    private void decrypt() throws Exception
    {
        File encrypted = new File(ENCRYPTED);
        File plaintext = new File(PLAINTEXT);
        int encryptedSize = (int)encrypted.length();
        BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream(encrypted));
        BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(plaintext));

        //Read in encrypted data
        byte[] input = new byte[encryptedSize];
        Path path = Paths.get(ENCRYPTED);
        input = Files.readAllBytes(path);

        if(input.length >= (SALT_SIZE*2 + MAC_SIZE))
        {
            byte[] cryptSalt = Arrays.copyOfRange(input, START, SALT_SIZE);
            byte[] macSalt = Arrays.copyOfRange(input, SALT_SIZE, SALT_SIZE*2);
            byte[] macBytes = Arrays.copyOfRange(input, SALT_SIZE*2, (SALT_SIZE*2 + MAC_SIZE));
            byte[] cryptBytes = Arrays.copyOfRange(input, (SALT_SIZE*2 + MAC_SIZE), input.length);

            //This generates the same MAC key from encryption.
            //Before, the KeyGenerator created a new random key
            //meaning the derived and computed MAC keys were never the same
            byte[] macKeyBytes = PBEKeyFactory.getKey(PASSWORD, macSalt, ITERATIONS, KEY_LENGTH);
            SecretKeySpec macKey = new SecretKeySpec(macKeyBytes, MAC_ALGORITHM);
            Mac mac = Mac.getInstance(MAC_ALGORITHM);
            mac.init(macKey);
            byte[] compMacBytes = mac.doFinal(cryptBytes);
            //Check if computed and derived MAC's are the same
            if(Arrays.equals(macBytes, compMacBytes))
            {
                //Creates same key from encryption 
                byte[] cryptKeyBytes = PBEKeyFactory.getKey(PASSWORD, cryptSalt, ITERATIONS, KEY_LENGTH);
                SecretKeySpec cryptKey = new SecretKeySpec(cryptKeyBytes, ALGORITHM);

                //Creates cipher and reads decrypted data to array
                Cipher cipher = Cipher.getInstance(TRANSFORMATION);
                cipher.init(Cipher.DECRYPT_MODE, cryptKey, new IvParameterSpec(new byte[16]));
                byte[] output = cipher.doFinal(cryptBytes);

                bufferedInputStream.close();
                bufferedOutputStream.write(output);   
                bufferedOutputStream.flush();
                bufferedOutputStream.close();
            }
        }
    }
}
//This class has only one method, getKey(), which returns a byte array
//of a SecretKey of the corresponding parameters
public class PBEKeyFactory
{
    private static final String KEY_DERIVATION_FUNCTION = "PBKDF2WithHmacSHA1";
    public static byte[] getKey(String password, byte[] salt, int iterations, int length) throws Exception
    {
        KeySpec keySpec = new PBEKeySpec(password.toCharArray(), salt, iterations, length);
        SecretKeyFactory factory = SecretKeyFactory.getInstance(KEY_DERIVATION_FUNCTION);
        return factory.generateSecret(keySpec).getEncoded();
    }
}
