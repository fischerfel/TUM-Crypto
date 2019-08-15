private static final String ALGORITHM = "AES/CBC/PKCS5Padding";
private static int ITERATIONS = 65536;
private static SecretKey secretKey = null;
final private static short KEY_LEN_IN_BITS = (short)128;
final private static byte[] BUFFER = new byte[1024 * 64];

private static byte[] generateSalt() throws NoSuchAlgorithmException {  
    Random random = SecureRandom.getInstance("SHA1PRNG");
    byte[] salt = new byte[8];
    random.nextBytes(salt);
    return salt;
}

static ArrayList<Object> encrypt256(final FileInputStream fis, final String encryptFileTarget, final String fileTitle, final String keyVal) throws Exception{

    final ArrayList<Object> encryptInfo = new ArrayList<Object>();

    try{
        byte[] saltBytes = new byte[8];
        saltBytes = generateSalt();

        SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        KeySpec spec = new PBEKeySpec(keyVal.toCharArray(), saltBytes, ITERATIONS, KEY_LEN_IN_BITS);
        secretKey = skf.generateSecret(spec);
        SecretKey secret = new SecretKeySpec(secretKey.getEncoded(), "AES");//"ISO-8859-1"

        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, secret);

        AlgorithmParameters params = cipher.getParameters ();
        byte[] initIVVector = params.getParameterSpec (IvParameterSpec.class).getIV();

        FileOutputStream fos = new FileOutputStream(encryptFileTarget, true);
        CipherOutputStream cos = new CipherOutputStream(fos, cipher);

        InputStream is = new BufferedInputStream(fis);
        int bytesRead = -1;
        int count = 0;

        while((bytesRead = is.read(BUFFER)) != -1){
            //System.out.println("bytesRead values is : " + bytesRead);
            cos.write(BUFFER, 0, bytesRead);
        }

        cos.flush();
        cos.close();
        is.close();
        fos.flush();
        fos.close();
        fis.close();

        encryptInfo.add(Hex.encodeHexString(initIVVector));
        encryptInfo.add(Hex.encodeHexString(saltBytes));

    }catch(Exception exp){
        exp.printStackTrace();
    }

    return encryptInfo;
}
