public class AESEncryption
{
private final String ALGORITHM = "AES";
private final String MAC_ALGORITHM = "HmacSHA256";
private final String TRANSFORMATION = "AES/CBC/PKCS5Padding";
private final String KEY_DERIVATION_FUNCTION = "PBKDF2WithHmacSHA1";
private final String PLAINTEXT = "/Volumes/CONNOR P/Unencrypted.txt";
private final String ENCRYPTED = "/Volumes/CONNOR P/Encrypted.txt";
private final String PASSWORD = "javapapers";
private final String LOC = Paths.get(".").toAbsolutePath().normalize().toString();
private static final int SALT_SIZE = 64;
private final int KEY_LENGTH = 128;
private final int ITERATIONS = 100000;

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
    int encryptedSize = (int)encrypted.length();
    int plaintextSize = (int)plaintext.length();

    BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream(PLAINTEXT));
    BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(ENCRYPTED));

    //Create salt
    byte[] salt = new byte[SALT_SIZE];
    SecureRandom secureRandom = new SecureRandom();
    secureRandom.nextBytes(salt);

    //Create cipher key    
    SecretKeyFactory factory = SecretKeyFactory.getInstance(KEY_DERIVATION_FUNCTION);
    KeySpec keySpec = new PBEKeySpec(PASSWORD.toCharArray(), salt, ITERATIONS, KEY_LENGTH);
    SecretKey secret = new SecretKeySpec(factory.generateSecret(keySpec).getEncoded(), ALGORITHM);

    //Create cipher
    Cipher cipher = Cipher.getInstance(TRANSFORMATION);
    cipher.init(Cipher.ENCRYPT_MODE, secret, new IvParameterSpec(new byte[16]));

    //Read plaintext file into byte array
    byte[] input = new byte[encryptedSize];
    Path path = Paths.get(PLAINTEXT);
    input = Files.readAllBytes(path);
    byte[] crypt = cipher.doFinal(input);

    //Create MAC object and apply to the byte array crypt[] containing the plaintext
    KeyGenerator keyGenerator = KeyGenerator.getInstance(MAC_ALGORITHM);
    SecretKey macKey = keyGenerator.generateKey();
    Mac mac = Mac.getInstance(MAC_ALGORITHM);
    mac.init(macKey);
    byte[] macBytes = mac.doFinal(crypt);

    //Add salt, MAC'd plaintext, and encrypted plaintext to final array
    byte[] output = new byte[SALT_SIZE + crypt.length + macBytes.length];
    System.arraycopy(salt, 0, output, 0, SALT_SIZE);
    System.arraycopy(macBytes, 0, output, SALT_SIZE, macBytes.length);
    System.arraycopy(crypt, 0, output, SALT_SIZE + macBytes.length, crypt.length);

    //Write array with encrypted data to a new file
    bufferedOutputStream.write(output);
    bufferedInputStream.close();   
    bufferedOutputStream.flush();
    bufferedOutputStream.close();
}
