    /**
    * parts of this code were copied from the StandardPBEByteEncryptor class from
    * the Jasypt (www.jasypt.org) project
    */
    public class AESCrypt
    {
    private final String KEY_ALGORITHM = "PBEWITHSHA1AND128BITAES-CBC-BC";
    // private final String KEY_ALGORITHM = "PBEWithSHA256And256BitAES-CBC-BC";
    private final String MODE_PADDING = "/CBC/PKCS5Padding";
    private final int DEFAULT_SALT_SIZE_BYTES = 128;
    private byte[] ivParamSpec1 = 
           {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};
    private final SecureRandom rand;
    private final String passwd = "kn4\"bE,W11kewsUd";
    public AESCrypt() throws Exception
    {
    rand = SecureRandom.getInstance("SHA1PRNG");
    }
    private byte[] generateSalt(int size)
    {
    byte[] salt = new byte[size];
    rand.nextBytes(salt);
    return salt;
    }
    private SecretKey generateKey(String algorithm, int keySize, byte[] salt)
    throws NoSuchProviderException, NoSuchAlgorithmException,
    InvalidKeySpecException
    {
    // SecretKeySpec Spec = new SecretKeySpec(keyBytes, algorithm);
    SecretKeyFactory factory = SecretKeyFactory.getInstance(KEY_ALGORITHM);
    PBEKeySpec pbeKeySpec = new PBEKeySpec(passwd.toCharArray(), salt,100000);
    SecretKey tmpKey = factory.generateSecret(pbeKeySpec);
    byte[] keyBytes = new byte[keySize / 8];
    SecretKeySpec Spec = new SecretKeySpec(keyBytes, algorithm);
    System.arraycopy(tmpKey.getEncoded(), 0, keyBytes, 0, keyBytes.length);
    System.out.println("SecretKeySpec(keyBytes, algorithm)---->"+Spec);
    return Spec;
    }
    private byte[] generateIV(Cipher cipher)
    {
    byte[] iv = new byte[cipher.getBlockSize()];
    rand.nextBytes(iv);
    return iv;
    }
    private byte[] appendArrays(byte[] firstArray, byte[] secondArray)
    {
    final byte[] result = new byte[firstArray.length + secondArray.length];
    System.arraycopy(firstArray, 0, result, 0, firstArray.length);
    System.arraycopy(secondArray, 0, result, firstArray.length, secondArray.length);
    return result;
    }
    public byte[] encrypt(String algorithm, int keySize, final byte[] message)
    throws Exception
    {
    Cipher cipher = Cipher.getInstance(algorithm + MODE_PADDING);
    // The salt size for the chosen algorithm is set to be equal
    // to the algorithm's block size (if it is a block algorithm).
    int saltSizeBytes = DEFAULT_SALT_SIZE_BYTES;
    int algorithmBlockSize = cipher.getBlockSize();
    if (algorithmBlockSize > 0)
    {
    saltSizeBytes = algorithmBlockSize;
    }
    // Create salt
    // final byte[] salt = generateSalt(saltSizeBytes);
    final byte[] salt = "EW0h0yUcDX72WU9UiKiCwDpXsJg=".getBytes();
    SecretKey key = generateKey(algorithm, keySize, salt);
    // create a new IV for each encryption
    // final IvParameterSpec ivParamSpec = new IvParameterSpec();
    ivParamSpec1 = "ixYgnjjY58RNacxZHwxgBQ==".getBytes();
    final IvParameterSpec ivParamSpec = new IvParameterSpec(ivParamSpec1);
    // Perform encryption using the Cipher
    cipher.init(Cipher.ENCRYPT_MODE, key, ivParamSpec);
    byte[] encryptedMessage = cipher.doFinal(message);
    // append the IV and salt
    encryptedMessage = appendArrays(ivParamSpec.getIV(), encryptedMessage);
    encryptedMessage = appendArrays(salt, encryptedMessage);
    return encryptedMessage;
    }
    public byte[] decrypt(String algorithm, int keySize, final byte[] encryptedMessage) throws Exception
    {
    Cipher cipher = Cipher.getInstance(algorithm + MODE_PADDING);
    // determine the salt size for the first layer of encryption
    int saltSizeBytes = DEFAULT_SALT_SIZE_BYTES;
    int algorithmBlockSize = cipher.getBlockSize();
    if (algorithmBlockSize > 0)
    {
    saltSizeBytes = algorithmBlockSize;
    }
    System.out.println("saltSizeBytes:" + saltSizeBytes);
    byte[] decryptedMessage = new byte[encryptedMessage.length];
    System.arraycopy(encryptedMessage, 0, decryptedMessage, 0,
    encryptedMessage.length);
    // extract the salt and IV from the incoming message
    byte[] salt = null;
    byte[] iv = null;
    byte[] encryptedMessageKernel = null;
    final int saltStart = 0;
    final int saltSize = (saltSizeBytes < decryptedMessage.length ? saltSizeBytes
    : decryptedMessage.length);
    // final int saltSize = 32;
    // System.out.println("saltSize:"+saltSize);
    final int ivStart = (saltSizeBytes < decryptedMessage.length ? saltSizeBytes
    : decryptedMessage.length);
    final int ivSize = cipher.getBlockSize();
    final int encMesKernelStart = (saltSizeBytes + ivSize < decryptedMessage.length ? saltSizeBytes
    + ivSize
    : decryptedMessage.length);
    final int encMesKernelSize = (saltSizeBytes + ivSize < decryptedMessage.length ? (decryptedMessage.length
    - saltSizeBytes - ivSize)
    : 0);
    salt = new byte[saltSize];
    iv = new byte[ivSize];
    System.out.println("saltSize:" + saltSize);
    System.out.println("ivSize:" + ivSize);
    encryptedMessageKernel = new byte[encMesKernelSize];
    System.out.println("encryptedMessageKernel");
    System.arraycopy(decryptedMessage, saltStart, salt, 0, saltSize);
    System.arraycopy(decryptedMessage, ivStart, iv, 0, ivSize);
    System.arraycopy(decryptedMessage, encMesKernelStart,
    encryptedMessageKernel, 0, encMesKernelSize);
    SecretKey key = generateKey(algorithm, keySize, salt);
    System.out.println("ekey");
    ivParamSpec1 = "ixYgnjjY58RNacxZHwxgBQ==".getBytes();
    IvParameterSpec ivParamSpec = new IvParameterSpec(ivParamSpec1);
    // Perform decryption using the Cipher
    cipher.init(Cipher.DECRYPT_MODE, key, ivParamSpec);
    decryptedMessage = cipher.doFinal(encryptedMessageKernel);
    // Return the results
    return decryptedMessage;
    }
    private byte[] decryptWithLWCrypto(byte[] cipher, String password, byte[] salt, final int iterationCount)
    throws Exception
    {
    PKCS12ParametersGenerator pGen = new PKCS12ParametersGenerator(new SHA256Digest());
    char[] passwordChars = password.toCharArray();
    final byte[] pkcs12PasswordBytes = PBEParametersGenerator.PKCS12PasswordToBytes(passwordChars);
    pGen.init(pkcs12PasswordBytes, salt, iterationCount);
    CBCBlockCipher aesCBC = new CBCBlockCipher(new AESEngine());
    ParametersWithIV aesCBCParams = (ParametersWithIV) pGen.generateDerivedParameters(256, 128);
    aesCBC.init(false, aesCBCParams);
    PaddedBufferedBlockCipher aesCipher = new PaddedBufferedBlockCipher(aesCBC,new PKCS7Padding());
    byte[] plainTemp = new byte[aesCipher.getOutputSize(cipher.length)];
    int offset = aesCipher.processBytes(cipher, 0, cipher.length, plainTemp, 0);
    int last = aesCipher.doFinal(plainTemp, offset);
    final byte[] plain = new byte[offset + last];
    System.arraycopy(plainTemp, 0, plain, 0, plain.length);
    System.out.println("Plain--->"+plain);
    return plain;
    }
    public static void main(String[] args) throws Exception
    {
    Security.addProvider(new BouncyCastleProvider());
    AESCrypt aesCrypt = new AESCrypt();
    String originalText = "6skYngWZxkTgsRIoFPLS1mpI32Q=";
    String toDecrypt = new String(Base64.encode(aesCrypt.encrypt("AES", 128, originalText.getBytes())));
    System.out.println("Original Data----->"+originalText);
    System.out.println("After Encrpytion--->"+toDecrypt);
    byte[] criptata = Base64.decode(toDecrypt);
    byte[] decriptata = aesCrypt.decrypt("AES", 128, criptata);
    String msgdecriptato = new String(decriptata);
    System.out.println("After Decrption--->"+msgdecriptato);
    if (!originalText.equals(msgdecriptato))
    {
    throw new IllegalStateException("Strings do not match!");
    }
    else
    {
    System.out.println("Matched.....");
    }
    // byte[] cipher = "RVcwaDB5VWNEWDcyV1U5VWlLaUN3RHBYc0pnPWl4WWduampZNThSTmFjeFoJfgJbigcnDs0TJdNu7yEkprlJFdilLrLJT8uoQ3dm/A==".getBytes();
    // String password = "kn4\"bE,W11kewsUd";
    // byte[] salt = "EW0h0yUcDX72WU9UiKiCwDpXsJg=".getBytes();
    // System.out.println(aesCrypt.decryptWithLWCrypto(cipher, password, salt , 128));
    }
    }
    When I try to run this... Am getting following Errors,
    Exception in thread "main" java.security.InvalidAlgorithmParameterException: Wrong IV length: must be 16 bytes long
    at com.sun.crypto.provider.SunJCE_f.a(DashoA13*..)
    at com.sun.crypto.provider.AESCipher.engineInit(DashoA13*..)
    at javax.crypto.Cipher.init(DashoA13*..)
    at javax.crypto.Cipher.init(DashoA13*..)
    at AESCrypt.encrypt(AESCrypt.java:113)
    at AESCrypt.main(AESCrypt.java:213)
