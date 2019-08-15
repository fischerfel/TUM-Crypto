public class TestSkip {
  public static final String ALGO = "AES/CBC/PKCS5Padding";
  public static final String CONTENT = "Strive not to be a success, but rather to be of value";
  private static int BlockSizeBytes = 16;
  private static SecureRandom random = null;

  static {
    try {
      random = SecureRandom.getInstance("SHA1PRNG");
    } catch (NoSuchAlgorithmException e) {
      throw new RuntimeException("Could not initialize AES encryption", e);
    }
  }

  static byte[] getKeyBytes() throws NoSuchAlgorithmException, UnsupportedEncodingException
  {
    byte[] key = "Not a secure string!".getBytes("UTF-8");
    MessageDigest sha = MessageDigest.getInstance("SHA-1");
    key = sha.digest(key);
    key = Arrays.copyOf(key, 16); // use only first 128 bit

    return key;
  }

  static KeySpec getKeySpec() throws GeneralSecurityException, UnsupportedEncodingException
  {
    return new SecretKeySpec(getKeyBytes(), "AES");
  }

  static byte[] getIv ()
  {
    byte[] iv = new byte[BlockSizeBytes];
    random.nextBytes(iv);

    return iv;
  }

  static Cipher initCipher (int mode, byte[] iv) throws GeneralSecurityException, UnsupportedEncodingException
  {
    KeySpec spec = getKeySpec();
    Cipher cipher = Cipher.getInstance(ALGO);
    cipher.init(mode, (SecretKey) spec, new IvParameterSpec(iv));
    return cipher;
  }

  static void encrypt(String fileName) throws
      GeneralSecurityException,
      IOException
  {
    FileOutputStream fos = new FileOutputStream(fileName);
    byte[] iv = getIv();
    fos.write(iv);

    Cipher cipher = initCipher(Cipher.ENCRYPT_MODE, iv);
    CipherOutputStream cos = new CipherOutputStream(fos, cipher);
    PrintWriter pw = new PrintWriter(cos);
    pw.println(CONTENT);
    pw.close();
  }

  static void skipAndCheck(String fileName) throws
      GeneralSecurityException,
      IOException
  {
    FileInputStream fis = new FileInputStream(fileName);
    byte[] iv = new byte[BlockSizeBytes];
    if (fis.read(iv) != BlockSizeBytes) {
      throw new GeneralSecurityException("Could not retrieve IV from AES encrypted stream");
    }

    Cipher cipher = initCipher(Cipher.DECRYPT_MODE, iv);
    CipherInputStream cis = new CipherInputStream(fis, cipher);

    // This does not skip
    long count = cis.skip(32);
    System.out.println("Bytes skipped: " + count);

    // Read a line
    InputStreamReader is = new InputStreamReader(cis);
    BufferedReader br = new BufferedReader(is);
    String read = br.readLine();
    System.out.println("Content after skipping 32 bytes is: " + read);

    br.close();
  }

  static InputStream getWrapper(CipherInputStream cis) {
    return new SkipInputStream(cis);
  }

  public static void main(String[] args) throws
      IOException,
      GeneralSecurityException
  {
    String fileName = "EncryptedSample.txt";

    encrypt(fileName);
    skipAndCheck(fileName); 
  }

}
