public class AESDecryption
{
private final String ALGORITHM = "AES";
private final String MAC_ALGORITHM = "HmacSHA256";
private final String TRANSFORMATION = "AES/CBC/PKCS5Padding";
private final String KEY_DERIVATION_FUNCTION = "PBKDF2WithHmacSHA1";
private final String PLAINTEXT = "/Volumes/CONNOR P/De-Encrypted.txt";
private final String ENCRYPTED = "/Volumes/CONNOR P/Encrypted.txt";
private final String PASSWORD = "javapapers";
private final String LOC = Paths.get(".").toAbsolutePath().normalize().toString();
private final int SALT_SIZE = 64;
private final int IV_SIZE = 16;
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
    int plaintextSize = (int)plaintext.length();
    BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream(encrypted));
    BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(plaintext));

    //Read in the encrypted data
    byte[] input = new byte[encryptedSize];
    Path path = Paths.get(ENCRYPTED);
    input = Files.readAllBytes(path);
    int increment = (input.length-SALT_SIZE)/2;

    if(input.length >= (SALT_SIZE + increment))
    {
        //Recover salt, MAC, and encrypted data and store in arrays
        byte[] salt = Arrays.copyOfRange(input, 0, SALT_SIZE);
        byte[] macBytes = Arrays.copyOfRange(input, SALT_SIZE, SALT_SIZE + increment);
        byte[] crypt = Arrays.copyOfRange(input, SALT_SIZE + increment, input.length);

        //Regenerate original MAC
        KeyGenerator keyGenerator = KeyGenerator.getInstance(MAC_ALGORITHM);
        SecretKey macKey = keyGenerator.generateKey();
        Mac mac = Mac.getInstance(MAC_ALGORITHM);
        mac.init(macKey);
        byte[] hmac = mac.doFinal(crypt);        

        if(Arrays.equals(macBytes, hmac)) //This is where it fails, never enters
        {
            //Regenerate cipher and decrypt data
            SecretKeyFactory factory = SecretKeyFactory.getInstance(KEY_DERIVATION_FUNCTION);
            KeySpec keySpec = new PBEKeySpec(PASSWORD.toCharArray(), salt, ITERATIONS, KEY_LENGTH);
            SecretKey secret = new SecretKeySpec(factory.generateSecret(keySpec).getEncoded(), ALGORITHM);

            Cipher cipher = Cipher.getInstance(TRANSFORMATION);
            cipher.init(Cipher.DECRYPT_MODE, secret, new IvParameterSpec(new byte[16]));

            //Write decrypted data to new text file
            byte[] output = cipher.doFinal(crypt);
            bufferedOutputStream.write(output);   
            bufferedInputStream.close();
            bufferedOutputStream.flush();
            bufferedOutputStream.close();
        }
    }
}
