// defined outside the below method; this algorithm works:
private static String ALGORITHM = "PBEWITHSHA1ANDDESEDE";

// the new version, which fails:
//private static String ALGORITHM = "PBEWithHmacSHA512AndAES_128";

private static Cipher getCipher(int mode, String password) throws NoSuchAlgorithmException, InvalidKeySpecException,
    NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException {

    // Create secret key using password
    PBEKeySpec pbeKeySpec = new PBEKeySpec(password.toCharArray());
    SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance(ALGORITHM);
    SecretKey secretKey = secretKeyFactory.generateSecret(pbeKeySpec);

    // Create the cipher
    byte[] salt = new byte[SALT_SIZE];
    salt = password.getBytes();

    PBEParameterSpec pbeParameterSpec = new PBEParameterSpec(salt, PBEPARAMETERSPEC_ITERATION_COUNT);
    Cipher cipher = Cipher.getInstance(ALGORITHM);

    // this original line causes crash with the new algorithm, reporting:
    // Exception in thread "main" java.security.InvalidAlgorithmParameterException: Missing parameter type: IV expected
    // solved as per this S.O. post:
    // https://stackoverflow.com/questions/29215274/how-do-i-properly-use-the-pbewithhmacsha512andaes-256-algorithm
    cipher.init(mode, secretKey, pbeParameterSpec);

    // this new line causes the encryption/decryption to apparently fail, giving results that look like this:
    // �0�.�����j�"��ۗP#o˾���IYc� �we����)�Tq(f�C���.��njDt�.pG��
    //cipher.init(mode, secretKey, cipher.getParameters());
    return cipher;
}
